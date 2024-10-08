package com.example.front.data.response.auth

import com.example.front.data.base.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("user") var user: User
)
