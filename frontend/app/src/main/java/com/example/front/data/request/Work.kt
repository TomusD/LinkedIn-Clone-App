package com.example.front.data.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Work(
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("date_started") val dateStarted: LocalDate,
    @SerializedName("date_ended") val dateEnded: LocalDate? = null
)

