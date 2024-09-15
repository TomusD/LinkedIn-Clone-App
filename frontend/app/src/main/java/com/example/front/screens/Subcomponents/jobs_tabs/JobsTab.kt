package com.example.front.screens.Subcomponents.jobs_tabs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import com.example.front.data.ApiClient
import com.example.front.data.response.APIResponse
import com.example.front.data.response.JobApplied
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun JobsTab(recommendedJobs: List<JobApplied> = emptyList(), onApply: (JobApplied) -> Unit, onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var jobIndex by remember { mutableStateOf(-1) }


    fun increaseView(jobId: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).increaseView(jobId)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "VIEW JOB GET --- ${response.body()}")

                } else {
                    val res = response.body()
                    Log.e("MYTEST", "VIEW JOB GET --- RESPONSE NOT SUCCESSFUL - $res")
                }
            }

            override fun onFailure(p0: Call<APIResponse>, p1: Throwable) {
                Log.e("MYTEST", "VIEW JOB GET --- FAILURE - ${p1.message}")
            }
        })
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        if (recommendedJobs.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(recommendedJobs.size) { index ->
                    Box(Modifier.padding(horizontal = 30.dp).fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 20.dp, vertical = 5.dp).background(Color.LightGray, RoundedCornerShape(10.dp)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {

                            Column(Modifier.padding(start = 10.dp)) {
                                Text(text = recommendedJobs[index].role, fontSize = 4.em, fontWeight = FontWeight.Bold)
                                Text(text = recommendedJobs[index].organization, fontSize = 3.em, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    increaseView(recommendedJobs[index].job_id)
                                    jobIndex = index
                                    showDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                Text(text = "View Job")
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "Recommendations are cooking. Until then, expand your network!",
                modifier = Modifier.padding(50.dp)
            )
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            JobCard(
                job = recommendedJobs[jobIndex],
                onApply = { onApply(it) },
                onDismiss = {showDialog= false},
            )
        }
    }
}
