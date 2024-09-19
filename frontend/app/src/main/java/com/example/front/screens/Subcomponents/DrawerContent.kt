package com.example.front.screens.Subcomponents

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.NavController
import com.example.front.ProfileActivity
import com.example.front.SettingsActivity

@Composable
fun DrawerContent(navController: NavController, onDestinationClicked: (route: String) -> Unit, onSettingsUpdate: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getBooleanExtra("exit", false)
            if (data == true) {
                onSettingsUpdate()
            }
        }
    }

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
                    val intent = Intent(context, SettingsActivity::class.java)
                    launcher.launch(intent)
                }
        )

        Text(
            text = "Logout",
            color = Color.Red,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onLogout()
                }
        )
    }
}