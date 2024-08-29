import uvicorn
from fastapi import Depends, FastAPI, HTTPException

from sqlalchemy.orm import Session
from helpers import read_env_properties
import crud, models, schemas
from database import SessionLocal, engine

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


@app.get("/")
def read_root():
    return {"message": "World"}


@app.post("/signup/")
def register_user(user: schemas.User, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_email(db, email=user.email)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_user(db=db, user=user)


if __name__ == "__main__":
    ip, port = read_env_properties()
    uvicorn.run("main:app", host=ip, reload=True, port=port, ssl_certfile=cert_path, ssl_keyfile=key_path)