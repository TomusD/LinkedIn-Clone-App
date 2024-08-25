package com.example.front

sealed class Screens(val route : String) {

    object Home: Screens("home_route")
    object Upload: Screens("upload_route")
    object Network: Screens("network_route")
    object Jobs: Screens("jobs_route")
    object Notifications: Screens("notifications_route")

}