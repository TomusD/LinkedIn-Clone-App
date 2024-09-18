from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Date, Float, Text, Table, DateTime, func
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

user_connection_association = Table(
    'user_relationship', Base.metadata,
    Column('requester_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('receiver_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('state', String)  
)

comment_post_association = Table(
    'comments', Base.metadata,
    Column('comment_id', Integer, primary_key=True, autoincrement=True),
    Column('post_id', Integer, ForeignKey('posts.post_id')),
    Column('user_id', Integer, ForeignKey('users.id')),
    Column('comment_text', String(250), nullable=False),
    Column("date_commented", DateTime, nullable=False, default=func.now())
)

like_post_association = Table(
    'likes', Base.metadata,
    Column('post_id', Integer, ForeignKey('posts.post_id'), primary_key=True),
    Column('user_id', Integer, ForeignKey('users.id'), primary_key=True)
)

user_chat_association = Table(
    'user_chat', Base.metadata,
    Column('user_id', Integer, ForeignKey('users.id'), primary_key=True),
    Column('chat_id', Integer, ForeignKey('chats.chat_id'), primary_key=True),
)

class Message(Base):
    __tablename__ = "message"

    message_id = Column(Integer, primary_key=True, autoincrement=True)
    chat_id = Column(Integer, ForeignKey('chats.chat_id'))
    sender_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    content = Column(String, nullable=False)
    datetime_sent = Column(DateTime, default=func.now())
    
    chat = relationship('Chat', back_populates='messages')

    def __repr__(self):
        return f"<Message(id={self.message_id}, content={self.content}, sender_id={self.sender_id})>"


class Chat(Base):
    __tablename__ = 'chats'
    
    chat_id = Column(Integer, primary_key=True)
    date_created = Column(Date, default=date.today())
    last_updated = Column(DateTime, default=func.now())
    
    users = relationship('User', secondary=user_chat_association, back_populates='chats', lazy='selectin')
    messages = relationship('Message', back_populates='chat')

    def __repr__(self):
        return f"<Chat(id={self.chat_id})>"
    
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
        secondary=user_connection_association,
        primaryjoin = id == user_connection_association.c.requester_id,
        secondaryjoin = id == user_connection_association.c.receiver_id,
        back_populates="connected_to",
        foreign_keys=[user_connection_association.c.requester_id, user_connection_association.c.receiver_id]

    )
    connected_to = relationship(
        'User',
        secondary=user_connection_association,
        primaryjoin = id == user_connection_association.c.receiver_id,
        secondaryjoin = id == user_connection_association.c.requester_id,
        foreign_keys=[user_connection_association.c.receiver_id, user_connection_association.c.requester_id],
        back_populates='connections'
    )
    
    chats = relationship('Chat', secondary=user_chat_association, back_populates='users', lazy='selectin')

    liked_posts = relationship("Post", secondary=like_post_association, back_populates="likers")
    uploaded_posts = relationship("Post")


    def get_connections(self):
        return self.connections + self.connected_to

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


class JobViews(Base):
    __tablename__ = "job_views"

    user_id = Column(Integer, ForeignKey('users.id'), primary_key=True)
    job_id = Column(Integer, ForeignKey('jobs.job_id'), primary_key=True)
    view_count = Column(Integer, default=0, nullable=False)


class Post(Base):
    __tablename__ = "posts"

    post_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    input_text = Column(Text, nullable=False)
    media_image_url = Column(String)
    media_video_url = Column(String)
    media_audio_url = Column(String)
    date_uploaded = Column(DateTime)

    commentors =  relationship("User", secondary=comment_post_association)
    likers = relationship("User", secondary=like_post_association, back_populates="liked_posts")

    def __repr__(self):
        return f"PID: {self.post_id} - TEXT: {self.input_text} - COMS: {self.commentors}"

class Notification(Base):
    __tablename__ = "notifications"

    notification_id = Column(Integer, primary_key=True, autoincrement=True)
    receiver_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    notifier_id = Column(Integer, ForeignKey('users.id'), nullable=False)
    post_id = Column(Integer, ForeignKey('posts.post_id'), nullable=True)
    notification_type = Column(String, nullable=False)
    is_resolved = Column(Boolean, default=False)
    date_created = Column(DateTime, default=func.now(), nullable=False)

    def __repr__(self):
        return f"Notification(id={self.notification_id}, receiver={self.receiver_id}, notifier={self.notifier_id}, type={self.notification_type}, resolved={self.is_resolved})"
