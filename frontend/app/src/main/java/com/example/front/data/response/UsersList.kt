package com.example.front.data.response

import com.example.front.data.base.User
import com.google.gson.annotations.SerializedName

data class UsersList(
    @SerializedName("users")
    var users: List<User>
)
