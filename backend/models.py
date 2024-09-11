from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Date, Float, Text, Table
from sqlalchemy.orm import relationship
from database import Base
from datetime import date

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

job_application_association = Table(
    'applications', Base.metadata,
    Column('job_id', Integer, ForeignKey('jobs.job_id'), primary_key=True),
    Column('applicant_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column("date_applied", Date, nullable=False, default=date.today())
)

user_user_association_table = Table(
    'user_relationship', Base.metadata,
    Column('requester_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('receiver_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('state', String)  

comment_post_association = Table(
    'comments', Base.metadata,
    Column('comment_id', Integer),
    Column('post_id', Integer, ForeignKey('posts.post_id')),
    Column('user_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('text', String(250), nullable=False),
    Column("date_applied", Date, nullable=False, default=date.today())
)

like_post_association = Table(
    'likes', Base.metadata,
    Column('post_id', Integer, ForeignKey('posts.post_id'), primary_key=True),
    Column('user_id', Integer, ForeignKey('users.id'), primary_key=True)
)

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String)
    surname = Column(String)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    image_path = Column(String)

    applications = relationship("Job", secondary=job_application_association, back_populates="applicants")
    connections = relationship(
        "User",
        secondary=user_association_table,
        primaryjoin=id == user_user_association_table.c.requester_id,
        secondaryjoin=id == user_user_association_table.c.receiver_id,
        backref="connected_to",
        foreign_keys=[user_user_association_table.c.requester_id, user_user_association_table.c.receiver_id]
    )

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
    recruiter_fullname = Column(String, nullable=False)
    organization = Column(String, nullable=False)
    role = Column(String, nullable=False)
    place = Column(String, nullable=False)
    type = Column(String, nullable=False)
    salary = Column(String, nullable=False)

    recruiter = relationship("User", backref="jobs")
    skills = relationship('Skill', secondary=job_skill_association, back_populates='jobs')

    applicants = relationship("User", secondary=job_application_association, back_populates="applications")


    def __repr__(self):
        return f"Job(jid={self.job_id}, uid={self.recruiter_id}, fullname={self.recruiter_fullname})"


class Post(Base):
    __tablename__ = "posts"

    post_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    input_text = Column(Text, nullable=False)
    media_image_url = Column(String)
    media_video_url = Column(String)
    media_sound_url = Column(String)
    date_uploaded = Column(Date)

    comments =  relationship("User", secondary=comment_post_association)
    likes =  relationship("User", secondary=like_post_association)
