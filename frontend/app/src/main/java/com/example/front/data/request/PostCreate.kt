package com.example.front.data.request

import android.net.Uri

data class PostCreate(
    val postText: String,
    val imageURI: Uri?,
    val videoURI: Uri?,
    val audioURI: Uri?,
)

