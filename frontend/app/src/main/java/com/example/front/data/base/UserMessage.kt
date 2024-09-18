package com.example.front.data.base

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserMessage(
    @SerializedName("message") val message: String,
    @SerializedName("sender_id") val sender_id: Int,
    @SerializedName("datetime_sent") val datetime_sent: LocalDateTime
)
