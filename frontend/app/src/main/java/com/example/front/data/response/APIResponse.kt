package com.example.front.data.response

import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("status_code") val status: Int?,
    @SerializedName("message") val message: String?,
)
