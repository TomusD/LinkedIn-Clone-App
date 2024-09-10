package com.example.front.screens.Subcomponents.jobs_tabs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.front.data.response.JobApplied
import com.example.front.data.response.JobUploaded
import com.example.front.screens.Subcomponents.modals.JobModal

@Composable
fun MyJobsTab(appliedJobs: List<JobApplied>? = emptyList(), createdJobs: List<JobUploaded>? = emptyList()) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    // Padding to move the header down
    Spacer(modifier = Modifier.height(20.dp))

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(start = 15.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {

        // Applications Section
        Text(text = "Applications",
            fontSize = 5.em,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth(),
        ) {
            if (appliedJobs!!.isNotEmpty())
                appliedJobs.forEach { job ->
                    JobCard(job, "Remove", onApplyClick = {})
                } else {
                Text(text = "You haven't applied to any job yet")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Jobs Created Section
        Text(text = "Jobs Uploaded",
            fontSize = 5.em,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            if (createdJobs!!.isNotEmpty())
                createdJobs.forEach { job ->
                    JobCard(job, button_text = "View Applicants", onApplyClick = {})
            } else {
                Text(text = "You haven't uploaded any job yet")
            }
        }
    }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {

        Text("+ Upload Job")
    }

    if (showDialog) {
        JobModal(
            onDismiss = { showDialog = false },
            onSave = { job ->
                showDialog = false
            }
        )
    }
}
