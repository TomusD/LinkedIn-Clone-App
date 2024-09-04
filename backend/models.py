from sqlalchemy import Boolean, Column, ForeignKey, Integer, String
from sqlalchemy.orm import relationship
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
        return f"<User(id={self.id}, fullname={self.name} {self.surname}, email={self.email}, image_path={self.image_path})>"

class Job(Base):
    __tablename__ = "job"

    job_id = Column(Integer, primary_key=True, autoincrement=True)
    recruiter_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    organization = Column(String)
    role = Column(String)
    place = Column(String)
    type = Column(String)
    salary = Column(Integer)

    recruiter = relationship("User", backref="jobs")

    def __repr__(self):
        return f"<Job(Job ID={self.job_id}, User ID={self.recruiter_id}/{User.id}, User(fullname={User.name} {User.surname})"

