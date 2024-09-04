from pydantic import BaseModel, EmailStr

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


class UserList(BaseModel):
    users: list[User]
