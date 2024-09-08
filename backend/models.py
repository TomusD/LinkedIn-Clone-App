from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Date, Float, DateTime, Table
from sqlalchemy.orm import relationship
from database import Base

"""
models.py

These models are used for interacting with the database
"""
user_info_skill_association = Table(
    'user_skills', Base.metadata,
    Column('user_id', Integer, ForeignKey('user_info.id')),
    Column('user_skill_name', String, ForeignKey('skills.skill_name'))
)

job_skill_association = Table(
    'job_skills', Base.metadata,
    Column('job_id', Integer, ForeignKey('jobs.job_id')),
    Column('job_skill_name', String, ForeignKey('skills.skill_name'))
)

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    surname = Column(String)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    image_path = Column(String)

    def __repr__(self) -> None:
        return f"<User(id={self.id}, fullname={self.name} {self.surname}, email={self.email}, image_path={self.image_path})>"


class UserInfo(Base):
    __tablename__ = "user_info"

    id = Column(Integer, ForeignKey('users.id'),  primary_key=True, nullable=False)
    education_public = Column(Boolean, default=True)
    work_public = Column(Boolean, default=True)
    skills_public = Column(Boolean, default=True)

    works = relationship("Work", back_populates="user_info")
    education = relationship("Education", back_populates="user_info")
    skills = relationship('Skill', secondary=user_info_skill_association, back_populates='users')
    
    def __repr__(self) -> None:
        return f"<User(id={self.id}, works={self.works}, edu={self.education}>)"


class Work(Base):
    __tablename__ = "work"

    work_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey('user_info.id'), nullable=False)
    organization = Column(String, nullable=False)
    role = Column(String, nullable=False)
    date_started = Column(Date, nullable=False)
    date_ended = Column(Date, nullable=True)

    user_info = relationship("UserInfo", back_populates="works")
    
    def __repr__(self) -> None:
        return f"<Work(id={self.work_id}, organization={self.organization}, role={self.role}, date_started={self.date_started}, date_ended={self.date_ended})>"


class Education(Base):
    __tablename__ = "education"

    edu_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey('user_info.id'), nullable=False)
    organization = Column(String, nullable=False)
    science_field = Column(String, nullable=False)
    degree = Column(Float, nullable=True)
    date_started = Column(Date, nullable=False)
    date_ended = Column(Date, nullable=True)

    user_info = relationship("UserInfo", back_populates="education")
    
    def __repr__(self) -> None:
        return f"<Edu(id={self.edu_id}, organization={self.organization}, science_field={self.science_field}, degree={self.degree}, date_started={self.date_started}, date_ended={self.date_ended})>"

    
class Skill(Base):
    __tablename__ = "skills"

    skill_name = Column(String, unique=True, primary_key=True)

    users = relationship('UserInfo', secondary=user_info_skill_association, back_populates='skills')
    jobs = relationship('Job', secondary=job_skill_association, back_populates='skills')

    def __repr__(self):
        return f"<Skill(Skill Name={self.skill_name})"
    

class Job(Base):
    __tablename__ = "jobs"

    job_id = Column(Integer, primary_key=True, autoincrement=True)
    recruiter_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    organization = Column(String, nullable=False)
    role = Column(String, nullable=False)
    place = Column(String, nullable=False)
    type = Column(String, nullable=False)
    salary = Column(Integer, nullable=False)

    recruiter = relationship("User", backref="jobs")

    skills = relationship('Skill', secondary=job_skill_association, back_populates='jobs')

    def __repr__(self):
        return f"<Job(Job ID={self.job_id}, User ID={self.recruiter_id}/{User.id}, User(fullname={User.name} {User.surname})"

class Applications(Base):
    __tablename__ = "applications"

    applier_id = Column(Integer, ForeignKey('users.id'),primary_key=True, nullable=False)
    job_id = Column(Integer, ForeignKey('jobs.job_id'),primary_key=True, nullable=False)
    date_applied = Column(DateTime, nullable=False)

    applier = relationship("User", backref="applications")
    job = relationship("Job", backref="applications")

    def __repr__(self):
        return f"<Applications(Applier ID={self.applier_id}/{User.id}, Job ID={self.job_id}/{Job.job_id}, Date Applied={self.date_applied})"
