package com.example.front.data.response

import com.example.front.data.response.SkillsList
import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("job_id") val job_id: Int = -1,
    @SerializedName("recruiter_id") val recruiter_id: Int = -1,
    @SerializedName("recruiter_name") val recruiter_name: String = "",
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("place") val place: String,
    @SerializedName("jobType") val jobType: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skillsList") val skillsList: SkillsList
)