package com.example.front.data.request

import com.google.gson.annotations.SerializedName

data class CommentCreate(
    @SerializedName("comment_text") val comment_text: String
)
