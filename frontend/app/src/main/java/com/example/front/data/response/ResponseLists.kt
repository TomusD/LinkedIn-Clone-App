package com.example.front.data.response

import com.example.front.data.base.User
import com.google.gson.annotations.SerializedName


data class UsersList(
    @SerializedName("users")
    var users: List<User>
)


data class WorkList(
    @SerializedName("workList")
    var workList: List<WorkResponse>
)


data class EducationList(
    @SerializedName("eduList")
    var eduList: List<EducationResponse>
)


// This data class is used both for Requests and Responses
data class SkillsList(
    @SerializedName("skills")
    var skills: List<String>
)


data class JobsList(
    @SerializedName("recommendations")
    var recommendations: List<JobApplied>
)


data class PostsList(
    @SerializedName("posts")
    var posts: List<PostResponse>
)