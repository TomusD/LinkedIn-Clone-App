package com.example.front.data.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class  Education(
    @SerializedName("organization") val organization: String,
    @SerializedName("science_field") val science_field: String,
    @SerializedName("degree") val degree: Float?,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)

