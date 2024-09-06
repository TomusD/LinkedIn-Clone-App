package com.example.front.screens.Subcomponents

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.front.ProfileActivity
import com.example.front.Screens

@Composable
fun DrawerContent(onDestinationClicked: (route: String) -> Unit) {
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Personal Profile",
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    // Navigate to Profile
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
        )
        Text(
            text = "Settings",
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onDestinationClicked(Screens.Settings.route)
                }
        )
    }
}