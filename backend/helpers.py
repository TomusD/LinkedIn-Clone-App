from os import getenv
from dotenv import load_dotenv

def read_env_properties(path: str = "./env") -> tuple[str, int]:
    load_dotenv()
    return getenv("ip"), int(getenv("port"))

def read_db_properties(path: str = "./env") -> tuple[str, str, str, str, str]:
    load_dotenv()
    return getenv("db_host"), \
            getenv("db_port"), \
            getenv("db_username"), \
            getenv("db_pwd"), \
            getenv("db_name")

def read_security_variables(path: str = "./env") -> tuple[str, str, str, float, float]:
    load_dotenv()
    return {
        "JWT_SECRET_KEY" : getenv("JWT_SECRET_KEY"),
        "JWT_REFRESH_SECRET_KEY" : getenv("JWT_REFRESH_SECRET_KEY"),
        "ALGORITHM" : getenv("ALGORITHM"),
        "ACCESS_TOKEN_EXPIRE_MINUTES" : getenv("ACCESS_TOKEN_EXPIRE_MINUTES"),
        "REFRESH_TOKEN_EXPIRE_MINUTES" : getenv("REFRESH_TOKEN_EXPIRE_MINUTES")
    }
