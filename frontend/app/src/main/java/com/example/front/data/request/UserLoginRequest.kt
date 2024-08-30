package com.example.front.data.request

import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)
