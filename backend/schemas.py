from pydantic import BaseModel, EmailStr, PastDate, PositiveFloat
from typing import Optional
from datetime import datetime

"""
schemas.py

These schemas are used for validating the schema of the classes
"""

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    email: str | None = None

class LoginUser(BaseModel):
    email: EmailStr
    password: str

class UserBase(BaseModel):
    name: str
    surname: str
    email: EmailStr
    image_path: str

# Inherits from UserBase
class UserRegister(UserBase):
    password: str

class User(UserBase):
    id: int

class LoginResponse(Token):
    user: User

class JobBase(BaseModel):
    organization: str
    role: str
    place: str
    type: str
    salary: int

class JobCreate(JobBase):
    pass
    
class JobResponse(JobCreate):
    job_id: int
    recruiter_id: int

# Profile Settings
class Settings(BaseModel):
    organization: str
    date_started: PastDate
    date_ended: Optional[PastDate] = None

class Work(Settings):
    role: str

class WorkResponse(Work):
    work_id: int

class Education(Settings):
    science_field: str
    degree: Optional[PositiveFloat] = None

class EduResponse(Education):
    edu_id: int

class Skills(BaseModel):
    skills: list[str]

# Responses as lists
class UserList(BaseModel):
    users: list[User]
    
class WorkList(BaseModel):
    workList: list[WorkResponse]

class EduList(BaseModel):
    eduList: list[EduResponse]
    
    
# Application Settings
class ApplicationBase(BaseModel):
    job_id: int

class ApplicationResponse(ApplicationBase):
    applier_id: int
    date_applied: datetime

# Skills Settings
class skillBase(BaseModel):
    skill_name: str

class addSkill(skillBase):
    pass
