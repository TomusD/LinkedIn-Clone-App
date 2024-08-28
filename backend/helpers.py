from os import getenv
from dotenv import load_dotenv

def read_env_properties(path: str = "./env") -> tuple[str, int]:
    load_dotenv()
    return getenv("ip"), int(getenv("port"))
