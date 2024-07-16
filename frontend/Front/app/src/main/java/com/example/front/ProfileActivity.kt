package com.example.front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.ui.theme.FrontEndTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                ProfileScreen()
            }
        }
    }

    @Composable
    fun ProfileScreen() {
        var experience by remember { mutableStateOf("") }
        var education by remember { mutableStateOf("") }
        var skills by remember { mutableStateOf("") }
        var isExperiencePublic by remember { mutableStateOf(false) }
        var isEducationPublic by remember { mutableStateOf(false) }
        var isSkillsPublic by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Professional Experience") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isExperiencePublic,
                    onCheckedChange = { isExperiencePublic = it }
                )
                Text("Public")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = education,
                onValueChange = { education = it },
                label = { Text("Education") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isEducationPublic,
                    onCheckedChange = { isEducationPublic = it }
                )
                Text("Public")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = skills,
                onValueChange = { skills = it },
                label = { Text("Skills") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isSkillsPublic,
                    onCheckedChange = { isSkillsPublic = it }
                )
                Text("Public")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* Handle Save button click */ }, modifier = Modifier.align(Alignment.End)) {
                Text("Save")
            }
        }
    }
}
