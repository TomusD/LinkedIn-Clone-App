from datetime import datetime, timedelta, timezone
import time
from jose import JWTError, jwt
from typing import Union, Any
from fastapi import HTTPException
import crud
from helpers import read_security_variables

security_var = read_security_variables()


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now(timezone.utc) + expires_delta
    else:
        expire = datetime.now(timezone.utc) + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, security_var["JWT_SECRET_KEY"], algorithm=security_var["ALGORITHM"])
    return encoded_jwt


def create_refresh_token(subject: Union[str, Any]) -> str:
    expire = timezone.utcnow() + timedelta(days=security_var["REFRESH_TOKEN_EXPIRE_DAYS"])
    to_encode = {"exp": expire, "sub": str(subject)}
    encoded_jwt = jwt.encode(to_encode, security_var["JWT_REFRESH_SECRET_KEY"], algorithm=security_var["ALGORITHM"])
    return encoded_jwt
