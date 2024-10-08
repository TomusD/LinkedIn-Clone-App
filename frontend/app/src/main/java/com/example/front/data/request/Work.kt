package com.example.front.data.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Work(
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)

