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

