package com.example.front.screens.Subcomponents.jobs_tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.front.data.response.JobApplied

@Composable
fun JobsTab(recommendedJobs: List<JobApplied> = emptyList()) {
    val context = LocalContext.current

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        if (recommendedJobs.isNotEmpty()) {
            JobList(jobs = recommendedJobs, onApplyClick = { /* Handle Apply */ })
            
        } else {
            Text(
                text = "Recommendations are cooking. Until then, expand your network!",
                modifier = Modifier.padding(50.dp)
            )
        }


//        if (workList.isNotEmpty()) {
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(workList) { work ->
//                    WorkInfo(work)
//                }
//            }
//        } else {
//            Text("No work experience added")
//        }
    }
}