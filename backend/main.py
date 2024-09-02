# FastAPI
import uvicorn
from fastapi import FastAPI, APIRouter, Depends, HTTPException, status, File, Form, UploadFile
from fastapi.responses import FileResponse, JSONResponse
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware
from fastapi.middleware.httpsredirect import HTTPSRedirectMiddleware

from typing import Annotated
from sqlalchemy.orm import Session
from jose import JWTError, jwt

# Project specific
import helpers
import crud, models, schemas
from datetime import timedelta
from database import SessionLocal, engine
import security as sec

# Python built-in
import time
import pathlib
import shutil

# Cloud Storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import storage

cred = credentials.Certificate("certifications/serviceAccountKey.json")
firebase_admin.initialize_app(cred, {
        'storageBucket': 'ergasiapp.appspot.com'

})
bucket = storage.bucket()# Get a reference to the storage bucket

cert_path: str = "./certifications/cert.pem"
key_path: str = "./certifications/key.pem"

path_public: str = "public/"
path_images: str = "images/"
path_profiles: str = "profiles/"
path_videos: str = "videos/"
path_sounds: str = "sounds/"


app = FastAPI()
app.mount("/static", StaticFiles(directory="public/", ), name="static")
models.Base.metadata.create_all(bind=engine)

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

security_var = helpers.read_security_variables()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

import copy
from typing import Any
@app.post("/users", response_class=JSONResponse, tags=["auth"])
async def create_user(    
    nameBody: str = Form(...), surnameBody: str = Form(...), emailBody: str = Form(...), 
    passwordBody: str = Form(...), image: UploadFile = File(...), db: Session = Depends(get_db)):


    db_user = crud.get_user_by_email(db, email=emailBody)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    file_name: str = (nameBody+surnameBody).lower() + str(int(time.time() * 1000)) + pathlib.Path(image.filename).suffix

    # Save the file locally
    local_image_copy = helpers.copy_upload_file(image)
    local_save_path: str = f"{path_public}{path_images}{path_profiles}{file_name}"
    with open(local_save_path, "wb") as buffer:
        shutil.copyfileobj(local_image_copy.file, buffer)

    # Save to Cloud Storage
    try:
        bucket = storage.bucket()
        cloud_save_path: str = f"{path_images}{path_profiles}{file_name}"
        blob = bucket.blob(cloud_save_path)
        
        blob.upload_from_string(
            image.file.read(),
            content_type=image.content_type
        )
        blob.make_public()

        download_url = blob.public_url
        print(f"Uploaded: {download_url}!\n")

    except Exception as e:
        print("error", str(e))
        return JSONResponse({"error": str(e)}, status_code=500)
    
    user = schemas.UserRegister(name=nameBody, 
                                surname=surnameBody, 
                                email=emailBody, 
                                password=passwordBody, 
                                image_path=download_url)

    
    crud.create_user(db=db, schema_user=user)

    return JSONResponse(content={"message": "Successfully registered!"}, status_code=200)


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
