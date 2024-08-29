from os import getenv
from dotenv import load_dotenv

def read_env_properties(path: str = "./env") -> tuple[str, int]:
    load_dotenv()
    return getenv("ip"), int(getenv("port"))

def read_db_properties(path: str = "./env"):
    load_dotenv()
    return getenv("db_host"), getenv("db_port"), getenv("db_username"), getenv("db_pwd"), getenv("db_name")