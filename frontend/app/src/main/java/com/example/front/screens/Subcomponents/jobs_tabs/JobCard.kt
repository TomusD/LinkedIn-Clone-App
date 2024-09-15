package com.example.front.screens.Subcomponents.jobs_tabs

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.data.ApiClient
import com.example.front.data.base.UserLittleDetail
import com.example.front.data.response.APIResponse
import com.example.front.data.response.JobApplied
import com.example.front.data.response.JobUploaded
import com.example.front.screens.Subcomponents.Chip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobCard(job: JobApplied, button_text: String = "Apply now", onApply: (JobApplied) -> Unit, onDismiss: () -> Unit,) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(10.dp),
    ) {

        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {

            if (button_text == "Apply now") {
                // Close Modal Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End).padding(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

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
                text = "${job.organization} • ${job.place} • ${job.type}",
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
                text = "Recruiter: ${job.recruiter_fullname}",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            // Display selected skills as chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                ,horizontalArrangement = Arrangement.spacedBy(0.dp)

            ) {
                job.skills.skills.forEach { skill ->
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
                onClick = {
                    if (button_text == "Revoke apply request") {
                        revokeApply(context, job.job_id)
                        onApply(job)
                        Toast.makeText(context, "You revoked the apply for this job!", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        onDismiss()
                        applyJob(context, job.job_id)
                        onApply(job)
                        Toast.makeText(context, "You applied for this job!", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (button_text=="Revoke apply request") Color(239, 71, 111)
                        else Color(17, 138, 178)
                )
            ) {
                Text(text = button_text)
            }
        }
    }
}

fun revokeApply(context: Context, jobId: Int) {
    val apiClient = ApiClient()
    val call = apiClient.getApiService(context).revokeApplyJob(jobId)
    call.enqueue(object : Callback<APIResponse> {
        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
            if (response.isSuccessful) {
                val res = response.body()
                Log.d("MYTEST", "REVOKE APPLY-SUCCESS: $res")
            }
        }

        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
            Log.e("MYTEST", "REVOKE APPLY-FAILURE: "+ t.message.toString())
        }
    })
}

fun applyJob(context: Context, jobId: Int) {
    val apiClient = ApiClient()

    val call = apiClient.getApiService(context).applyJob(jobId)

    call.enqueue(object : Callback<APIResponse> {
        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
            if (response.isSuccessful) {
                val res = response.body()
                Log.d("MYTEST", "APPLY-SUCCESS: $res")
            }
        }

        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
            Log.e("MYTEST", "APPLY-FAILURE: "+ t.message.toString())
        }
    })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobCard(job: JobUploaded, button_text: String = "View Applicants", onApplyClick: (List<UserLittleDetail>) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
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
                text = "${job.organization} • ${job.place} • ${job.type}",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Salary
            Text(
                text = "Salary: ${job.salary} 💵",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            // Display selected skills as chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                ,horizontalArrangement = Arrangement.spacedBy(0.dp)

            ) {
                job.skills.skills.forEach { skill ->
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
                onClick = { onApplyClick(job.applicants_list) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(text = button_text)
            }
        }
    }
}