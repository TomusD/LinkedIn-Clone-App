package com.example.front.data.response

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") val name: String?,
    @SerializedName("surname") val surname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("image_path") val imagePath: String?,
)
