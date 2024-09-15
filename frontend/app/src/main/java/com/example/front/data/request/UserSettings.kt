package com.example.front.data.request

import com.google.gson.annotations.SerializedName

data class UserSettings(
    @SerializedName("old_password") val old_password: String,
    @SerializedName("new_password") val new_password: String?,
    @SerializedName("new_email") val new_email: String?,
)
