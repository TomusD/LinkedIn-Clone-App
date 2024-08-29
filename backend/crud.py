from sqlalchemy.orm import Session
from datetime import datetime
from uuid6 import uuid7
from helpers import *

import schemas, models

"""
crud.py

Operations to interact with the database
"""

def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()

def create_user(db: Session, user: schemas.User):
    print("inside")
    db_user = models.User(
        name=user.name, 
        surname=user.surname,
        email=user.email,
        hashed_password=user.password,
        image_path=user.image_path
        )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    print(db.query(models.User).all())
    return db_user
