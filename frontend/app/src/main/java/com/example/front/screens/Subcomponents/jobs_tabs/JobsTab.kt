package com.example.front.screens.Subcomponents.jobs_tabs

import BasicViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.data.base.Job
import com.example.front.data.response.JobResponse
import com.example.front.data.response.SkillsList
import com.example.front.data.response.WorkResponse
import com.example.front.screens.Subcomponents.modals.WorkModal
import com.example.front.screens.Subcomponents.profile.WorkInfo
import com.example.front.screens.user.ToggleButton

@Composable
@Preview
fun JobsTab() {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    // Padding to move the header down
//    Spacer(modifier = Modifier.height(5.dp))

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        val sampleJobs = listOf(
            JobResponse(0,1, "John Doe","Software Engineer", "TechCorp", "New York", "Office", "$100,000", SkillsList(
                listOf("Kotlin", "Risk Management", "SQL Query Optimization", "Python")
            )),
            JobResponse(1,2, "Jane Smith","Product Manager", "InnovateX", "San Francisco", "Hybrid", "$80,000", SkillsList(emptyList())),
            JobResponse(2, 3, "Chris Johnson","Data Scientist", "Data Solutions", "Athens", "Remote", "$120,000", SkillsList(emptyList()))
        )

        JobList(jobs = sampleJobs, onApplyClick = { /* Handle Apply */ })

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