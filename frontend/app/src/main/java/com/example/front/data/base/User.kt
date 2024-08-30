package com.example.front.data.base

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") val name: String?,
    @SerializedName("surname") val surname: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("image_path") val imagePath: String?,
)
