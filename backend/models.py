from sqlalchemy import Boolean, Column, ForeignKey, Integer, String
from database import Base

"""
models.py

These models are used for interacting with the database
"""

class User(Base):  
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    surname = Column(String)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    image_path = Column(String)

    def __repr__(self) -> None:
        return f"<User(fullname={self.name} {self.surname}, email={self.email})"
