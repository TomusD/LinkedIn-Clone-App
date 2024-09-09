package com.example.front.data.base

import com.example.front.data.response.SkillsList
import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("organization") val organization: String,
    @SerializedName("role") val role: String,
    @SerializedName("place") val place: String,
    @SerializedName("type") val type: String,
    @SerializedName("salary") val salary: String,
    @SerializedName("skills") val skills: SkillsList,
)