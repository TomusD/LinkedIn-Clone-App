package com.example.front.data.response

import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("message") val message: String?,
)
