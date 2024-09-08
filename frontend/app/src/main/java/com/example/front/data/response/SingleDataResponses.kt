package com.example.front.data.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class WorkResponse(
    @SerializedName("work_id") val work_id: Int,
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)

data class EducationResponse(
    @SerializedName("edu_id") val edu_id: Int,
    @SerializedName("organization") val organization: String,
    @SerializedName("science_field") val science_field: String,
    @SerializedName("degree") val degree: Float?,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)
