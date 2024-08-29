from pydantic import BaseModel

"""
schemas.py

These schemas are used for validating the schema of the classes
"""

class UserBase(BaseModel):
    name: str
    surname: str
    email: str
    password: str
    image_path: str


# inherits
class User(UserBase):
    id: int
