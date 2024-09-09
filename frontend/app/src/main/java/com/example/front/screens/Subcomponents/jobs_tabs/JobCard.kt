package com.example.front.screens.Subcomponents.jobs_tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.data.response.JobResponse
import com.example.front.screens.Subcomponents.Chip

@Composable
fun JobList(jobs: List<JobResponse>, onApplyClick: (JobResponse) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(jobs.size) { index ->
            JobCard(job = jobs[index], onApplyClick = { onApplyClick(jobs[index]) })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobCard(job: JobResponse, onApplyClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Job Title (Role)
            Text(
                text = job.role,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            // Organization, Place, Job Type in one line
            Text(
                text = "${job.organization} • ${job.place} • ${job.jobType}",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Salary
            Text(
                text = "Salary: ${job.salary} 💵",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Recruiter
            Text(
                text = "Recruiter: ${job.recruiter_name} 👨🏻‍💼",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            // Display selected skills as chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)

            ) {
                job.skillsList.skills.forEach { skill ->
                    key(skill) { // Use `key` to ensure stable identity for each skill
                        Chip(
                            text = " $skill",
                            onDelete = { },
                            jobSkill = true
                        )
                    }
                }
            }


            // Apply Button (Full-width)
            Button(
                onClick = onApplyClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(17, 138, 178)
                )
            ) {
                Text(text = "Apply now")
            }
        }
    }
}