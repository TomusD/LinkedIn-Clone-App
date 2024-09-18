package com.example.front

sealed class PreAuthScreens(val route : String) {
    object Welcome: Screens("welcome_route")
    object Signin: Screens("signin_route")
    object Signup: Screens("signup_route")
}

sealed class Screens(val route : String) {
    object Home: Screens("home_route")
    object Upload: Screens("upload_route")
    object Network: Screens("network_route")
    object Jobs: Screens("jobs_route")
    object Notifications: Screens("notifications_route")
    object Profile: Screens("profile_route")
    object Settings : Screens("settings_route")
}

sealed class ChatScreens(val route : String) {
    object Chat: Screens("chat_route")
    object PersonalChat: Screens("personal_chat_route/{userId}")
}
