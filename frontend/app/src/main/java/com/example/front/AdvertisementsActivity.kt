package com.example.front

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.ui.theme.FrontEndTheme

class AdvertisementsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                AdvertisementsScreen()
            }
        }
    }

    @Composable
    fun AdvertisementsScreen() {
        var advertisements by remember { mutableStateOf(listOf("Ad 1: Job opening at Company X", "Ad 2: Freelance opportunity", "Ad 3: Internship at Startup Y")) }
        var myAdvertisements by remember { mutableStateOf(listOf("My Ad 1: Looking for a designer", "My Ad 2: Developer needed")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Advertisements",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // List of advertisements
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(advertisements) { advertisement ->
                    AdvertisementItem(advertisement)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Post new advertisement button
            Button(
                onClick = { /* Navigate to Post New Advertisement Screen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post New Advertisement")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "My Advertisements",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // List of user's own advertisements
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myAdvertisements) { myAd ->
                    MyAdvertisementItem(myAd)
                }
            }
        }
    }

    @Composable
    fun AdvertisementItem(advertisement: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(advertisement, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle Apply */ }) {
                    Text("Apply")
                }
            }
        }
    }

    @Composable
    fun MyAdvertisementItem(myAd: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(myAd, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle View Applications */ }) {
                    Text("View Applications")
                }
            }
        }
    }
}
