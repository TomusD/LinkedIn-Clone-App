import uvicorn
from fastapi import FastAPI, APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
import helpers
import crud, models, schemas
from database import SessionLocal, engine
from typing import Annotated
from jose import JWTError, jwt
from datetime import datetime, timedelta, timezone

import security as sec

cert_path: str = "./certifications/cert.pem"
key_path: str = "./certifications/key.pem"

models.Base.metadata.create_all(bind=engine)

app = FastAPI()

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

security_var = helpers.read_security_variables()

route = APIRouter(prefix="/auth", tags=["Authentication"])
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# @route.post("/login")
# def login(payload: schemas.LoginUser, db: Session = Depends(get_db)):
#     if not payload.email:
#         raise HTTPException(
#             status_code=status.HTTP_403_FORBIDDEN,
#             detail="Please add Phone number",
#         )
    
#     user = crud.get_user_by_email(db, payload.email)
#     if not user:
#         raise HTTPException(status_code=400, detail="Incorrect username or password")

#     # token =  create_access_token(user.id, timedelta(minutes=30)) 
#     # refresh = create_refresh_token(user.id,timedelta(minutes = 1008))

#     return {'access_token': token, 'token_type': 'bearer','refresh_token':refresh,"user_id":user.id}

@app.post("/users", tags=["auth"])
def create_user(user: schemas.UserRegister, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)


@app.post("/token", response_model=schemas.UserLoginResponse, tags=["auth"])
async def login_for_access_token(form_data: schemas.LoginUser, db: Session = Depends(get_db)) -> schemas.Token:

    user = crud.authenticate_user(db, form_data.email, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=float(security_var["ACCESS_TOKEN_EXPIRE_MINUTES"]))
    access_token = sec.create_access_token(
        data={"sub": user.email}, expires_delta=access_token_expires
    )

    return schemas.UserLoginResponse(access_token=access_token, token_type="bearer", id=str(user.id))


def get_current_user(token: Annotated[str, Depends(oauth2_scheme)], db: Session = Depends(get_db)):
    credentials_exception = HTTPException(
        status_code=401,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"})
    
    try:
        payload = jwt.decode(token, security_var["JWT_SECRET_KEY"], security_var["ALGORITHM"])
        
        if (email := payload.get("sub")) is None:
            raise credentials_exception
    
        token_data = schemas.TokenData(username=email)
        user = crud.get_user_by_email(db=db, username=token_data.email)
        
        if (user) is None:
            raise credentials_exception
       
        return user
    
    except JWTError:
        raise credentials_exception


if __name__ == "__main__":
    ip, port = helpers.read_env_properties()
    uvicorn.run(app, host=ip, port=port, ssl_certfile=cert_path, ssl_keyfile=key_path)
