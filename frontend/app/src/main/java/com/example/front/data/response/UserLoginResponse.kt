package com.example.front.data.response

import com.example.front.data.base.User
import com.google.gson.annotations.SerializedName

data class UserLoginResponse (
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("id") var uid: String?
)
