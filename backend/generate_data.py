import schemas
import models
import crud
from database import SessionLocal, engine
from sqlalchemy.orm import Session

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def generate_skills():
    # 10 Programming Languages
    programming_languages = {
        "Python", 
        "JavaScript", 
        "Java", 
        "C++", 
        "C#", 
        "Ruby", 
        "Go", 
        "Swift", 
        "Kotlin", 
        "PHP"
    }

    # 10 Soft Skills
    soft_skills = {
        "Communication", 
        "Teamwork", 
        "Problem Solving", 
        "Adaptability", 
        "Time Management", 
        "Critical Thinking", 
        "Leadership", 
        "Creativity", 
        "Emotional Intelligence", 
        "Conflict Resolution"
    }

    # 10 Hard Skills
    hard_skills = {
        "Data Analysis", 
        "Project Management", 
        "Technical Writing", 
        "SEO", 
        "Database Management", 
        "Cybersecurity", 
        "UI/UX Design", 
        "Cloud Computing", 
        "Machine Learning", 
        "DevOps"
    }

    all_skills = programming_languages | soft_skills | hard_skills

    #print(all_skills)
    return all_skills

def add_all_skills_to_db():
    db = next(get_db())
    all_skills = generate_skills()
    
    for skill_name in all_skills:
        skill_schema = schemas.addSkill(skill_name=skill_name)
        crud.add_skills(db, skill_schema)
    print("Skills added successfully.")

add_all_skills_to_db()

