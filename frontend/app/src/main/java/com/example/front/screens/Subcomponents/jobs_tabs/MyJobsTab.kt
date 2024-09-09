package com.example.front.screens.Subcomponents.jobs_tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.front.data.response.JobResponse
import com.example.front.data.response.SkillsList
import com.example.front.screens.Subcomponents.modals.JobModal

@Composable
@Preview
fun MyJobsTab() {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    // Padding to move the header down
    Spacer(modifier = Modifier.height(20.dp))

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {

            Text("+ Upload Job")
        }

        var sampleJobs = listOf(
            JobResponse(1,2, "Jane Smith","Product Manager", "InnovateX", "San Francisco", "Hybrid", "$80,000", SkillsList(emptyList())),
            JobResponse(2, 3, "Chris Johnson","Data Scientist", "Data Solutions", "Athens", "Remote", "$120,000", SkillsList(emptyList()))
        )

        if (showDialog) {
            JobModal(
                onDismiss = { showDialog = false },
                onSave = { job ->
                    showDialog = false
                    sampleJobs + job
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()

        ) {


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
}