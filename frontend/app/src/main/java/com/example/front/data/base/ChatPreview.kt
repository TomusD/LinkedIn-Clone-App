package com.example.front.data.base

import com.google.gson.annotations.SerializedName

data class ChatPreview(
    @SerializedName("chat_id") val chat_id: Int,
    @SerializedName("other_user") val other_user: UserLittleDetail,
    @SerializedName("date_created") val date_created: String,
    @SerializedName("last_updated") val last_updated: String,
)
