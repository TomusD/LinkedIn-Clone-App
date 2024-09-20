package com.example.front.data.base

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("notification_id") val notification_id: Int,
    @SerializedName("notifier") val notifier: UserLittleDetail,
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("notification_type") val notification_type: String,
    @SerializedName("date_created") val date_created: String
)
