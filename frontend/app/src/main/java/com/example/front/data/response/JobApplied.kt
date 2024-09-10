package com.example.front.data.response

import com.example.front.data.base.Job
import com.example.front.data.base.UserApplier
import com.example.front.data.response.SkillsList
import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("job_id") val job_id: Int = -1,
    @SerializedName("recruiter_id") val recruiter_id: Int = -1,
    @SerializedName("recruiter_fullname") val recruiter_fullname: String = "",
    @SerializedName("role") val role: String,
    @SerializedName("organization") val organization: String,
    @SerializedName("place") val place: String,
    @SerializedName("jobType") val jobType: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skills") val skills: SkillsList,
)

data class JobUpload(
    @SerializedName("job_id") val job_id: Int = -1,
    @SerializedName("role") val role: String,
    @SerializedName("organization") val organization: String,
    @SerializedName("place") val place: String,
    @SerializedName("jobType") val jobType: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skills") val skills: SkillsList,
    @SerializedName("applicants_list") val applicants: List<UserApplier> = emptyList()
)

data class AllJobs(
    @SerializedName("jobs_applied") val jobs_applied: List<JobResponse>,
    @SerializedName("jobs_uploaded") val jobs_uploaded: List<JobUpload>,
)