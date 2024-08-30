from sqlalchemy.orm import Session
from datetime import datetime
from uuid6 import uuid7
from helpers import *

import hashing
import schemas, models

"""
crud.py

Operations to interact with the database
"""

def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()


def create_user(db: Session, user: schemas.UserRegister):
    hashed_pwd = hashing.hash_password(user.password)

    db_user = models.User(
        name=user.name, 
        surname=user.surname,
        email=user.email,
        hashed_password=hashed_pwd,
        image_path=user.image_path
        )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    print(db.query(models.User).all())
    return db_user


def authenticate_user(db: Session, email: str, password: str):
    user = get_user_by_email(db, email)
    if not user:
        return False
    if not hashing.verify_password(password, user.hashed_password):
        return False
    return user
