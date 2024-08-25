package com.example.front

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label="Feed",
                icon=Icons.Filled.Home,
                route=Screens.Home.route
            ),
            BottomNavigationItem(
                label="Upload",
                icon=Icons.Filled.AddCircle,
                route=Screens.Upload.route
            ),
            BottomNavigationItem(
                label="Network",
                icon=Icons.Filled.Share,
                route=Screens.Network.route
            ),
            BottomNavigationItem(
                label="Jobs",
                icon=Icons.AutoMirrored.Filled.List,
                route=Screens.Jobs.route
            ),
            BottomNavigationItem(
                label="Notifications",
                icon=Icons.Filled.Notifications,
                route=Screens.Notifications.route
            ),
        )
    }
}
