from typing import Union

from fastapi import FastAPI

from models import User

app = FastAPI()


@app.get("/")
def read_root():
    return {"message": "World"}


@app.post("/signup")
def register_user(user: User):
    print(user)
    return {"message": "OK"}



@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}
