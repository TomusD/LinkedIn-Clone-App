from sqlalchemy.orm import Session, load_only
from datetime import datetime
from uuid6 import uuid7
from helpers import *

import hashing
import schemas, models

"""
crud.py

Operations to interact with the database
"""

def get_user_by_email(db: Session, email: str):
    return db.query(models.User).filter(models.User.email == email).first()

def get_users(db: Session, uid: str):
    users = db.query(models.User).all()
    print(users)
    return users

def create_user(db: Session, schema_user: schemas.UserRegister):
    hashed_pwd = hashing.hash_password(schema_user.password)

    db_user = models.User(
        name=schema_user.name, 
        surname=schema_user.surname,
        email=schema_user.email,
        hashed_password=hashed_pwd,
        image_path=schema_user.image_path
        )
    
    db.add(db_user)
    db.commit()
    db.refresh(db_user)

    db_user_info = models.UserInfo(id=db_user.id)
    db.add(db_user_info)
    db.commit()

    return schemas.UserRegister(name=db_user.name, surname=db_user.surname, password=db_user.hashed_password, email=db_user.email, image_path=db_user.image_path)

def authenticate_user(db: Session, email: str, password: str):
    user = get_user_by_email(db, email)
    if not user:
        return False
    if not hashing.verify_password(password, user.hashed_password):
        return False
    return user

def get_all_skills(db: Session):
    return db.query(models.Skill).all()



# Jobs 
def create_job(db: Session, job: schemas.JobBase, recruiter_id: int):
    model_skill = models.Skill
    job_skills = db.query(model_skill).filter(model_skill.skill_name.in_(job.skills_list.skills)).all()

    db_job= models.Job(
        recruiter_id=recruiter_id,
        organization=job.organization,
        role=job.role,
        place=job.place,
        type=job.type,
        salary=job.salary
    )

    db_job.skills = job_skills

    db.add(db_job)
    db.commit()
    db.refresh(db_job)
    return "OK"
 

def get_job(db: Session, job_id: int):
    job = db.query(models.Job).filter(models.Job.job_id == job_id).first()
    return job


def apply_job(db: Session, schema_application: schemas.ApplicationBase, applier_id: int):
    db_application = models.Applications(
        applier_id=applier_id,
        job_id=schema_application.job_id,
        date_applied=datetime.now()
    )
    db.add(db_application)
    db.commit()
    db.refresh(db_application)
    return schemas.ApplicationBase(applier_id=db_application.applier_id, job_id=db_application.job_id, date_applied=db_application.date_applied)





# Profile
def add_work_experience(db: Session, user_id: int, schema_work: schemas.Work):
    db_work = models.Work(
        user_id= user_id,
        organization = schema_work.organization,
        role=schema_work.role,
        date_started = schema_work.date_started,
        date_ended = (None if (schema_work.date_ended == None) else schema_work.date_ended)
    )
    db.add(db_work)
    db.commit()
    return "OK"


def add_education(db: Session, user_id: int, schema_edu: schemas.Education):
    db_edu = models.Education(
        user_id= user_id,
        organization = schema_edu.organization,
        science_field = schema_edu.science_field,
        degree = (None if (schema_edu.degree == None) else schema_edu.degree),
        date_started = schema_edu.date_started,
        date_ended = (None if (schema_edu.date_ended == None) else schema_edu.date_ended)
    )

    db.add(db_edu)
    db.commit()
    return "OK"


def add_skills(db: Session, user_id: int, updated_skills: schemas.Skills):
    model_user = models.UserInfo
    model_skill = models.Skill
    
    user_info = db.query(model_user).filter(model_user.id==user_id).first()    
    new_skills = db.query(model_skill).filter(model_skill.skill_name.in_(updated_skills.skills)).all()

    user_info.skills = new_skills
    
    db.commit()
    return "OK"


def get_user_info(db: Session, user_id: int):
    """
    Fetch work, education and skills of a user
    """
    return db.query(models.UserInfo).filter(models.UserInfo.id==user_id).first()


def change_publicity(db: Session, user_id: int, information: str):
    mod = models.UserInfo

    if information == "work":
        columm = mod.work_public
    elif information == "education":
        columm = mod.education_public
    elif information == "skills":
        columm = mod.skills_public
    else:
        return "BAD"
    
    db.query(mod).filter(mod.id==user_id).update({columm: ~columm})
    db.commit()
    return "OK"
    

def get_publicity(db: Session, user_id: int):
    res = db.query(models.UserInfo).filter(models.UserInfo.id==user_id).first()
    return res



# Only for generating data
def add_predefined_skills_to_db(db: Session, schema_skill: schemas.addSkill):
    db_skill = models.Skill(
        skill_name=schema_skill.skill_name
    )
    db.add(db_skill)
    db.commit()
    db.refresh(db_skill)
    return schemas.addSkill(skill_name=db_skill.skill_name)

