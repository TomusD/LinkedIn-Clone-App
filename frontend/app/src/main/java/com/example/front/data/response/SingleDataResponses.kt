package com.example.front.data.response

import com.example.front.data.base.UserLittleDetail
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class WorkResponse(
    @SerializedName("work_id") val work_id: Int,
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)

data class EducationResponse(
    @SerializedName("edu_id") val edu_id: Int,
    @SerializedName("organization") val organization: String,
    @SerializedName("science_field") val science_field: String,
    @SerializedName("degree") val degree: Float?,
    @SerializedName("date_started") val date_started: String,
    @SerializedName("date_ended") val date_ended: String?
)

data class PostResponse(
    @SerializedName("post_id") val post_id: Int,
    @SerializedName("user") val user: UserLittleDetail,
    @SerializedName("input_text") val input_text: String,
    @SerializedName("image_url") val image_url: String?,
    @SerializedName("video_url") val video_url: String?,
    @SerializedName("audio_url") val audio_url: String?,
    @SerializedName("date_uploaded") val date_uploaded: String,
    @SerializedName("comments") var comments: MutableList<Comments>,
    @SerializedName("likes") var likes: Int,
    @SerializedName("user_liked") var user_liked: Boolean,
)