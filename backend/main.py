from typing import Union
from fastapi import FastAPI
from models import User
import ssl
import uvicorn
from helpers import read_env_properties

cert_path: str = "./certifications/cert.pem"
key_path: str = "./certifications/key.pem"

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

if __name__ == "__main__":
    ip, port = read_env_properties()
    uvicorn.run(app=app, host=ip, port=port, ssl_certfile=cert_path, ssl_keyfile=key_path)