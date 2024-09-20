from sqlalchemy.orm import Session
import crud
import random

def similarity_score(user_skills, job_skills):
    user_set = set(user_skills)
    job_set = set(job_skills)
    intersection = len(user_set.intersection(job_set))
    return intersection / len(job_set)

# Skill recommendations algorithm based on Jaccard similarity
def skill_recommendations(db: Session, user_id: int):
    user_skills = crud.get_user_skills(db, user_id)
    jobs = set(crud.get_all_jobs(db, user_id))
    recommended_jobs = []

    for job in jobs:
        job_skills = crud.get_job_skills(db, job.job_id)
        similarity = similarity_score(user_skills, job_skills)
        if similarity > 0:
            recommended_jobs.append((job.job_id, similarity))
    recommended_jobs.sort(key=lambda x: x[1], reverse=True)

    return recommended_jobs

def dot_product(vector1, vector2):
    return sum(x * y for x, y in zip(vector1, vector2))

# Create the user-job matrix
def create_user_job_matrix(db: Session):
    users, jobs, job_views = crud.get_user_job_matrix(db)
    user_ids = sorted([user[0] for user in users])
    job_ids = sorted([job[0] for job in jobs])
    matrix = [[0 for _ in range(len(job_ids))] for _ in range(len(user_ids))]
    
    # Fill the matrix with the view counts
    for view in job_views:
        user_idx = user_ids.index(view.user_id)
        job_idx = job_ids.index(view.job_id)
        matrix[user_idx][job_idx] = view.view_count
    
    return matrix, user_ids, job_ids

#print("User-Job Matrix:\n", create_user_job_matrix(db))

# Matrix factorization recommendations algorithm
def matrix_factorization_recommendations(db: Session, user_id: int, latent_factors=2, learning_rate=0.01, epochs=5000, reg_param=0.02):
    matrix, user_ids, job_ids = create_user_job_matrix(db)
    num_users = len(user_ids)
    num_jobs = len(job_ids)
    
    # Initialize user and job latent factor matrices randomly
    user_factors = [[random.random() for _ in range(latent_factors)] for _ in range(num_users)]
    job_factors = [[random.random() for _ in range(latent_factors)] for _ in range(num_jobs)]
    
    # Perform matrix factorization using gradient descent
    for epoch in range(epochs):
        for i in range(num_users):
            for j in range(num_jobs):
                if matrix[i][j] > 0:
                    error = matrix[i][j] - dot_product(user_factors[i], job_factors[j])
                    # Update latent factors
                    for k in range(latent_factors):
                        user_factors[i][k] += learning_rate * (2 * error * job_factors[j][k] - reg_param * user_factors[i][k])
                        job_factors[j][k] += learning_rate * (2 * error * user_factors[i][k] - reg_param * job_factors[j][k])

    ''' 
    Reconstructed matrix
    reconstructed_matrix = [[dot_product(user_factors[i], job_factors[j]) for j in range(num_jobs)] for i in range(num_users)]
    print("RECONSTRUCTED MATRIX:\n", reconstructed_matrix)
    '''

    # Predict views for the users
    user_idx = user_ids.index(user_id)
    predicted_ratings = {}
    
    for j in range(num_jobs):
        predicted_rating = dot_product(user_factors[user_idx], job_factors[j])
        predicted_ratings[job_ids[j]] = predicted_rating
    sorted_recommendations = sorted(predicted_ratings.items(), key=lambda x: x[1], reverse=True)

    # Filter out jobs the user created or applied for
    user_created_jobs = crud.get_user_created_jobs(db, user_id)
    user_created_jobs = [job.job_id for job in user_created_jobs]
    user_applied_jobs = crud.get_user_applied_jobs(db, user_id)
    user_applied_jobs = [job.job_id for job in user_applied_jobs]

    filtered_recommendations = [
        job for job in sorted_recommendations 
        if job[0] not in user_created_jobs and job[0] not in user_applied_jobs
    ]
    
    return filtered_recommendations

# Hybrid recommendations algorithm
def hybrid_algorithm(db: Session, user_id: int):
    sr = skill_recommendations(db, user_id)
    mfr = matrix_factorization_recommendations(db, user_id)
    
    # Normalize the matrix factorization scores
    mfr_scores = [job[1] for job in mfr]
    min_mfr = min(mfr_scores)
    max_mfr = max(mfr_scores)
    normalized_mfr_scores = [(score - min_mfr) / (max_mfr - min_mfr) if max_mfr != min_mfr else 0 for score in mfr_scores]
    mfr = [(mfr[i][0], normalized_mfr_scores[i]) for i in range(len(mfr))]

    sr_dict = {job[0]: job[1] for job in sr}
    mfr_dict = {job[0]: job[1] for job in mfr}

    # Combine the recommendations
    combined_recommendations = {}
    for job_id, score in sr_dict.items():
        combined_recommendations[job_id] = score * 0.5
    for job_id, score in mfr_dict.items():
        if job_id in combined_recommendations:
            combined_recommendations[job_id] += score * 0.5
        else:
            combined_recommendations[job_id] = score * 0.5
    
    sorted_combined_recommendations = sorted(combined_recommendations.items(), key=lambda x: x[1], reverse=True)
    sorted_combined_recommendations = sorted_combined_recommendations[:10]

    
    return sorted_combined_recommendations

#print("Hybrid Recommendations: ", hybrid_algorithm(db, 5))