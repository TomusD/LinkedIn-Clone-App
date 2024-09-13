package com.example.front.data.base

import com.google.gson.annotations.SerializedName

data class UserLittleDetail(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("user_fullname") val user_fullname: String,
    @SerializedName("image_url") val image_url: String
)
