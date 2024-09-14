package com.example.front

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.front.data.base.User
import com.example.front.screens.user.FriendProfileScreen
import com.example.front.ui.theme.FrontEndTheme

class FriendProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val user = intent.getSerializableExtra("user") as User
            FrontEndTheme {
                FriendProfileScreen(user)
            }
        }
    }
}
