package com.example.front.data.response

import com.example.front.data.base.ChatPreview
import com.example.front.data.base.User
import com.example.front.data.base.UserMessage
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

data class UserInfo(
    @SerializedName("work") val work: WorkList?,
    @SerializedName("education") val education: EducationList?,
    @SerializedName("skills") val skills: SkillsList?,
    @SerializedName("is_friend") val is_friend: Boolean?

)



data class JobsList(
    @SerializedName("recommendations")
    var recommendations: List<JobApplied>
)


data class PostsList(
    @SerializedName("posts")
    var posts: List<PostResponse>
)

data class ChatsList(
    @SerializedName("chatsPreviews")
    var chatsPreviews: List<ChatPreview>
)

data class MessagesList(
    @SerializedName("messages")
    var messages: List<UserMessage>?
)