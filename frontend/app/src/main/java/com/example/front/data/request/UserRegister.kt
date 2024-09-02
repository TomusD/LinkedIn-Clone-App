package com.example.front.data.request

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class UserRegister (
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("image_uri") val imageURI: Uri?,
)