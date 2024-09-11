# FastAPI
import uvicorn
from fastapi import FastAPI, APIRouter, Depends, HTTPException, status, File, Form, UploadFile
from fastapi.responses import FileResponse, JSONResponse
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from fastapi.staticfiles import StaticFiles

from typing import Annotated
from sqlalchemy.orm import Session
from jose import JWTError, jwt

# Project specific
import helpers
import crud, models, schemas
from datetime import timedelta ,datetime, date
from database import SessionLocal, engine
import security as sec

# Python built-in
import time
import pathlib
import shutil

# Cloud Storage
import firebase_admin
from firebase_admin import credentials
from firebase_admin import storage

cred = credentials.Certificate("certifications/serviceAccountKey.json")
firebase_admin.initialize_app(cred, {
        'storageBucket': 'ergasiapp.appspot.com'

})
bucket = storage.bucket() # Get a reference to the storage bucket

cert_path: str = "./certifications/cert.pem"
key_path: str = "./certifications/key.pem"

path_public: str = "public/"
path_images: str = "images/"
path_videos: str = "videos/"
path_sounds: str = "sounds/"
path_profiles: str = "profiles/"
path_posts: str = "posts/"


app = FastAPI()
app.mount("/static", StaticFiles(directory="public/", ), name="static")
models.Base.metadata.create_all(bind=engine)

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

security_var = helpers.read_security_variables()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    credentials_exception = HTTPException(
        status_code=401,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"})
    
    try:
        payload = jwt.decode(token, security_var["JWT_SECRET_KEY"], security_var["ALGORITHM"])
        email = payload.get("sub")
        if email is None:
            raise credentials_exception
    
        token_data = schemas.TokenData(email=email)
        user = crud.get_user_by_email(db=db, email=token_data.email)
        
        if (user) is None:
            raise credentials_exception
       
        return user
    
    except JWTError:
        raise credentials_exception


@app.get("/users")
def get_users(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    users: list[models.User] = crud.get_users(db, current_user.id)
    schema_users: list[schemas.User] =  [schemas.User(id =u.id, 
                                               name = u.name, 
                                               surname = u.surname, 
                                               email = u.email, 
                                               image_path = u.image_path) for u in users]
    
    return schemas.UserList(users=schema_users)


@app.post("/users", response_class=JSONResponse, tags=["auth"])
async def create_user(    
    nameBody: str = Form(...), surnameBody: str = Form(...), emailBody: str = Form(...), 
    passwordBody: str = Form(...), image: UploadFile = File(...), db: Session = Depends(get_db)):


    db_user = crud.get_user_by_email(db, email=emailBody)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    file_name: str = (nameBody+surnameBody).lower() + str(int(time.time() * 1000)) + pathlib.Path(image.filename).suffix

    # Save the file locally
    local_image_copy = helpers.copy_upload_file(image)
    local_save_path: str = f"{path_public}{path_images}{path_profiles}{file_name}"
    with open(local_save_path, "wb") as buffer:
        shutil.copyfileobj(local_image_copy.file, buffer)

    # Save to Cloud Storage
    try:
        bucket = storage.bucket()
        cloud_save_path: str = f"{path_images}{path_profiles}{file_name}"
        blob = bucket.blob(cloud_save_path)
        
        blob.upload_from_string(
            image.file.read(),
            content_type=image.content_type
        )
        blob.make_public()

        download_url = blob.public_url
        print(f"Uploaded: {download_url}!\n")

    except Exception as e:
        print("error", str(e))
        return JSONResponse({"error": str(e)}, status_code=500)
    
    user = schemas.UserRegister(name=nameBody, 
                                surname=surnameBody, 
                                email=emailBody, 
                                password=passwordBody, 
                                image_path=download_url)

    
    crud.create_user(db=db, schema_user=user)

    return JSONResponse(content={"message": "Successfully registered!"}, status_code=200)


@app.post("/token", response_model=schemas.LoginResponse, tags=["auth"])
async def login_for_access_token(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)) -> schemas.Token:
    user = crud.authenticate_user(db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=float(security_var["ACCESS_TOKEN_EXPIRE_MINUTES"]))
    access_token = sec.create_access_token(
        data={"sub": user.email}, expires_delta=access_token_expires
    )

    return schemas.LoginResponse(access_token=access_token, 
                                 token_type="bearer", 
                                 user=schemas.User(
                                     id=user.id, 
                                     name= user.name, 
                                     surname= user.surname, 
                                     email= user.email, 
                                     image_path= user.image_path
                                 ))


# Posts
@app.post("/posts", response_class=JSONResponse, tags=["posts"])
async def create_post(    
    text_field: str = Form(...), media_image: UploadFile | None = None, 
    media_video: UploadFile | None = None, media_sound: UploadFile | None = None, 
    current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):

    media_dict = {"image": None, "video": None, "audio": None}
    
    image_url, video_url, sound_url = None, None, None
    if media_image is not None: 
        image_url, media_dict = save_to_cloud(media_image, "image", media_dict)

    if media_video is not None: 
        video_url, media_dict = save_to_cloud(media_video, "video", media_dict)

    if media_sound is not None: 
        sound_url, media_dict = save_to_cloud(media_sound, "audio", media_dict)


    post = schemas.Post(
        user_id= current_user.id,
        input_text= text_field,
        image_url= image_url,
        video_url= video_url,
        sound_url= sound_url,
        date_uploaded= date.today(),
    )

    crud.create_post(db, post)
    return JSONResponse(content={"message": "Post created Successfully!"}, status_code=200)



# Job API's
@app.post("/jobs", response_class=JSONResponse, tags=["jobs"])
async def create_job(job : schemas.JobBase, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.create_job(db=db, job=job, recruiter_id=current_user.id)
    return JSONResponse(content={"message": "Job created Successfully!"}, status_code=200)


@app.post("/jobs/{job_id}/apply", response_class=JSONResponse, tags=["jobs"])
async def create_application(job_id: int, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    job = crud.get_job(db, job_id)

    # Check if the user is trying to apply to their own job
    if not job:
        return "Job does not exists"
    if current_user.id == job.recruiter_id:
        raise HTTPException(status_code=400, detail="You cannot apply to your own job!")
    
    crud.apply_job(db=db, job_id=job_id, applier_id=current_user.id)
    return JSONResponse(content={"message": "Application created Successfully!"}, status_code=200)

@app.delete("/jobs/{job_id}/revoke-apply", response_class=JSONResponse, tags=["jobs"])
async def delete_application(job_id: int, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.revoke_apply_job(db=db, job_id=job_id, applier_id=current_user.id)
    return JSONResponse(content={"message": "Application revoked!"}, status_code=200)


@app.get("/user/jobs/recommended", response_model=schemas.JobsList, tags=["jobs"])
async def get_recommended_jobs(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    db_recommendations = crud.get_recommended_jobs(db, current_user.id)
    recommendations = [schemas.JobApplied(
                        organization=r.organization,
                        role=r.role,
                        place=r.place,
                        type=r.type,
                        salary=r.salary,
                        skills=schemas.Skills(skills=[skill.skill_name for skill in r.skills]),
                        job_id=r.job_id,
                        recruiter_id=r.recruiter_id,
                        recruiter_fullname=r.recruiter_fullname,    
                    ) for r in db_recommendations]
    
    return schemas.JobsList(recommendations=recommendations)


@app.get("/user/jobs", tags=["jobs"])
async def get_jobs(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    db_applications = crud.get_applications(db, current_user.id)
    applications = [schemas.JobApplied(
                        organization=a.organization,
                        role=a.role,
                        place=a.place,
                        type=a.type,
                        salary=a.salary,
                        skills=schemas.Skills(skills=[skill.skill_name for skill in a.skills]),
                        job_id=a.job_id,
                        recruiter_id=a.recruiter_id,
                        recruiter_fullname=a.recruiter_fullname,    
                    ) for a in db_applications]

    db_my_jobs = crud.get_uploaded_jobs(db, current_user.id)
    my_jobs = [schemas.JobUploaded(
                    organization=a.organization,
                    role=a.role,
                    place=a.place,
                    type=a.type,
                    salary=a.salary,
                    skills=schemas.Skills(skills=[skill.skill_name for skill in a.skills]),
                    job_id=a.job_id,
                    applicants_list=[schemas.UserLittleDetail(user_id=u.id, 
                                                    user_fullname=f"{u.name} {u.surname}",
                                                    image_url=u.image_path)
                                                    for u in a.applicants],
                ) for a in db_my_jobs]
        
    return schemas.AllJobs(jobs_applied=applications, jobs_uploaded=my_jobs)
    




# Profile APIs (Work, Education, Skills)
@app.post("/profile/work", response_class=JSONResponse, tags=["profile"])
async def update_work(work: schemas.Work, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.add_work_experience(db, current_user.id, work)
    return JSONResponse(content={"message": "Successfully added work!"}, status_code=200)


@app.post("/profile/education", response_class=JSONResponse, tags=["profile"])
async def update_edu(edu: schemas.Education, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.add_education(db, current_user.id, edu)
    return JSONResponse(content={"message": "Successfully added education!"}, status_code=200)


@app.put("/profile/skills", response_class=JSONResponse, tags=["profile"])
async def update_skills(skills: schemas.Skills, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.add_skills(db, current_user.id, skills)
    return JSONResponse(content={"message": "Successfully added skills!"}, status_code=200)


@app.get("/profile/work/me", response_model=schemas.WorkList, tags=["profile"])
async def get_work(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    ui = crud.get_user_info(db, current_user.id)
    work_list: list[schemas.WorkResponse] = [schemas.WorkResponse(work_id=u.work_id, 
                                                        organization=u.organization,
                                                        role=u.role,
                                                        date_started=u.date_started, 
                                                        date_ended=u.date_ended) 
                                                        for u in ui.works]
    return schemas.WorkList(workList=work_list)


@app.get("/profile/edu/me", response_model=schemas.EduList, tags=["profile"])
async def get_education(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    ui = crud.get_user_info(db, current_user.id)
    edu_list: list[schemas.EduResponse] = [schemas.EduResponse(edu_id=u.edu_id, 
                                                        organization=u.organization,
                                                        science_field=u.science_field,
                                                        degree=u.degree,
                                                        date_started=u.date_started, 
                                                        date_ended=u.date_ended) 
                                                        for u in ui.education]
                            
    return schemas.EduList(eduList=edu_list)


@app.get("/profile/skills/{user_id}", response_model=schemas.Skills)
async def get_skills(user_id: int, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    ui = crud.get_user_info(db, user_id)
    return schemas.Skills(skills=[skill.skill_name for skill in ui.skills])


@app.put("/profile/publicity/{information}/", response_class=JSONResponse, tags=["profile"])
async def change_publicity(information: str, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    crud.change_publicity(db, current_user.id, information)
    return JSONResponse(content={"message": f"Successfully changed {information} publicity!"}, status_code=200)


@app.get("/profile/publicity/all/{user_id}", response_class=JSONResponse, tags=["profile"])
async def get_publicity(user_id: int, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    obj = crud.get_publicity(db, user_id)
    return {"work": obj.work_public, "education": obj.education_public, "skills":obj.skills_public}


@app.get("/skills/available", response_model=schemas.Skills)
async def get_all_skills(current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    skills = crud.get_all_skills(db)
    return schemas.Skills(skills=[skill.skill_name for skill in skills])





# Friend requests
@app.post("/friends/request/{friend_id}", response_class=JSONResponse, tags=["friends"])
async def send_friend_request(friend_id: int, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    friend_req = crud.get_friend_request(db, current_user.id, friend_id)
    if friend_id == current_user.id:
        return JSONResponse(content={"message": "You cannot send a friend request to yourself!"}, status_code=400)
    if friend_req:
        return JSONResponse(content={"message": "You have a friend request from this user!"}, status_code=400)
    crud.friend_request(db, current_user.id, friend_id)
    return JSONResponse(content={"message": "Friend request sent!"}, status_code=200)


@app.put("/users/connect/{requester_id}/{accept}", response_class=JSONResponse, tags=["friends"])
async def handle_friend_request(requester_id: int, accept: bool, current_user: dict = Depends(get_current_user), db: Session = Depends(get_db)):
    if accept:
        crud.accept_friend_request(db, requester_id, current_user.id)
        return JSONResponse(content={"message": "Friend request accepted!"}, status_code=200)
    else:
        crud.reject_friend_request(db, requester_id, current_user.id)
        return JSONResponse(content={"message": "Friend request rejected!"}, status_code=200)


	
	
def save_to_cloud(file: UploadFile, media_type: str, media_dict: dict):

    try:
        extension = pathlib.Path(file.filename).suffix

        if media_type == "image":
            if extension not in (".jpg", ".png"):
                raise HTTPException(detail="Image must be a .jpg or a .png file", status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE)
            dir = path_images+path_posts
        
        elif media_type == "video":
            if extension != ".mp4":
                raise HTTPException(detail="Video must be an .mp4 file", status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE)
            dir = path_videos+path_posts
        else:
            if extension != ".mp3":
                raise HTTPException(detail="Audio must be an .mp3 file", status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE)
            dir = path_sounds+path_posts

        file_name: str = media_type + "_" + str(int(time.time() * 1000)) + extension

        # Save to Cloud Storage
        cloud_save_path: str = dir + file_name
        blob = bucket.blob(cloud_save_path)
        
        blob.upload_from_string(
            file.file.read(),
            content_type=file.content_type
        )
        blob.make_public()
        download_url = blob.public_url

        print(f"Uploaded {file.content_type}: {download_url}!\n")

        media_dict[media_type] = file_name

        return download_url, media_dict

    except Exception as e:
        print("MEDIA DICT ===", media_dict)
        for m in ["image", "video", "audio"]:
            if media_dict[m] is not None:
                path = f"{m}s/{path_posts}{media_dict[m]}"
                blob = bucket.blob(path)
                blob.delete()
                print(f"DELETED {media_dict[m]} --> {path}")
    
        # print("Error -->", str(e))
        raise HTTPException(detail=str(e), status_code=500)


# Entry point
if __name__ == "__main__":
    ip, port = helpers.read_env_properties()
    uvicorn.run("__main__:app", host=ip, port=port, reload=True, ssl_certfile=cert_path, ssl_keyfile=key_path)
