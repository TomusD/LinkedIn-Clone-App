package com.example.front.data.response

import com.example.front.data.base.UserLittleDetail
import com.google.gson.annotations.SerializedName

data class JobApplied(
    @SerializedName("job_id") val job_id: Int = -1,
    @SerializedName("recruiter_id") val recruiter_id: Int = -1,
    @SerializedName("recruiter_fullname") val recruiter_fullname: String = "",
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("place") val place: String,
    @SerializedName("type") val type: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skills") val skills: SkillsList,
)

data class JobUploaded(
    @SerializedName("job_id") val job_id: Int = -1,
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("place") val place: String,
    @SerializedName("type") val type: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skills") val skills: SkillsList,
    @SerializedName("applicants_list") val applicants_list: List<UserLittleDetail> = emptyList()
)

data class AllJobs(
    @SerializedName("jobs_applied") val jobs_applied: List<JobApplied>?,
    @SerializedName("jobs_uploaded") val jobs_uploaded: List<JobUploaded>?,
)