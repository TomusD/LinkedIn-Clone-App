# Backend - FastAPI
Steps to run locally the backend server follow:

### | First download and set postgres, then execute the steps below.

1. Create a `.env` properties file with the properties below:
    ```env
    ip=<IPv4> if test the app from mobile (if testing from emulator, put 'localhost')
    port=<port>
    
    db_host=<db_host> (e.g. localhost)
    db_port=<db_port> (usually 5432)
    db_username=<username-db-credential>
    db_pwd=<password-db-credential>
    db_name=<name_of_database>
    
    JWT_SECRET_KEY=<secter_jwt>
    ALGORITHM=<encryption-algorithm>
    ACCESS_TOKEN_EXPIRE_MINUTES=30
    REFRESH_TOKEN_EXPIRE_MINUTES=18000
    timeout=60
    adminapikey="your secret key if needed"
    ```

2. Install virtualenv module with `pip install virtualenv`
3. Create a virtual environment by running `python -m venv venv`
4. Activate venv with `. venv/Scripts/activate`
5. Install requirements.txt with `pip install -r requirements.txt`
6. Create a Firebase Google Cloud Storage (find relative steps from the site) and add the `serviceAccountKey.json` inside `\certifications`
7. Generate a public key `cert.pem` and a private key `key.pem` and put the files inside `\certifications`
8. Run server in development mode with `python main.py` where IP is the IPv4 (the same you put in android's `local.properties`)
