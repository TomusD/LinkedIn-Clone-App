package com.example.front.data.request

import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String
)
