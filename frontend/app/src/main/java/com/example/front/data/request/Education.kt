package com.example.front.data.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Education(
    @SerializedName("organization") val organization: String,
    @SerializedName("degree") val degree: Float,
    @SerializedName("field") val field: String,
    @SerializedName("date_started") val dateStarted: LocalDate,
    @SerializedName("date_ended") val dateEnded: LocalDate? = null
)

