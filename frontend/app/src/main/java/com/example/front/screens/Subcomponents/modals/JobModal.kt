package com.example.front.screens.Subcomponents.modals

import BasicViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.front.data.ApiClient
import com.example.front.data.base.Job
import com.example.front.data.response.APIResponse
import com.example.front.data.response.SkillsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JobModal(
    onDismiss: () -> Unit,
    onSave: (Job) -> Unit,
    viewModel: BasicViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAvailableSkills(context)
    }
    var availableSkills = viewModel.availableSkillsList.collectAsState().value


    var expanded by remember { mutableStateOf(false) }
    var selectedSkills by remember { mutableStateOf(listOf<String>()) }
    var selectedSkill by remember { mutableStateOf<String?>(null) }

    var role by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    fun uploadJob(job: Job) {
        val apiClient = ApiClient()

        val call = apiClient.getApiService(context).uploadJob(job)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()

                    Log.d("MYTEST", res.toString())

                } else {
                    Log.e("MYTEST", "RESPONSE NOT SUCCESSFUL:$response")
                    // Handle error
                }
            }
            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("MYTEST", "TOTAL FAILURE")
            }
        })
    }

    fun validateAndSave() {
        try {
            Log.d("MYTEST", "IM INSIDE")
            if (organization.isBlank() || role.isBlank() || type.isBlank() || place.isBlank() || salary.isBlank() || selectedSkills.isEmpty()) {
                errorMessage = "Please fill all required fields and add at least on skill."
                Log.e("MYTEST", errorMessage.toString())
            } else {
                if (type !in listOf("Office", "Remote", "Hybrid")) {
                    throw Exception()
                }
                salary.toFloat()
                errorMessage = null

                val jobObject = Job(organization, role, place, type, salary, SkillsList(selectedSkills.toList()))
                uploadJob(jobObject)
                Toast.makeText(context, "Re-visit to see your uploaded Job", Toast.LENGTH_SHORT).show()
                onSave(jobObject)
            }
        } catch (e: NumberFormatException) {
            errorMessage = "Degree must be a number."
            Log.e("MYTEST", errorMessage.toString())
        } catch (e: Exception) {
            errorMessage = "Type must be 'Office', 'Remote' or 'Hybrid'."
            Log.e("MYTEST", errorMessage.toString())

        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(5.dp),
        title = { Text(text = "+ Upload Job") },
        text = {
            Column(modifier = Modifier.fillMaxSize().padding(0.dp, 10.dp)) {

                TextField(
                    value = organization, onValueChange = { organization = it },
                    label = { Text(text = "Organization") },
                    placeholder = { Text(text = "Organization") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = role, onValueChange = { role = it },
                    label = { Text(text = "Role") },
                    placeholder = { Text(text = "Role") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = place, onValueChange = { place = it },
                    label = { Text(text = "Place") },
                    placeholder = { Text(text = "Place") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = type, onValueChange = { type = it },
                    label = { Text(text = "Type") },
                    placeholder = { Text(text = "Office/Remote/Hybrid") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = salary, onValueChange = { salary = it },
                    label = { Text(text = "Salary") },
                    placeholder = { Text(text = "Salary") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedButton(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,

                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Add skills")

                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableSkills.forEach { skill ->
                        DropdownMenuItem(
                            text = { Text(text = skill) },
                            onClick = {
                                if (skill !in selectedSkills) {
                                    selectedSkills = selectedSkills + skill
                                    Log.d("MYTEST", "JOB MODAL - $selectedSkills")
                                }
                                expanded = false
                            }
                        )
                    }
                }


                val scrollState = rememberScrollState()

                // Display selected skills as chips
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .verticalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)

                ) {
                    selectedSkills.forEach { skill ->
                        key(skill) { // Use `key` to ensure stable identity for each skill
                            com.example.front.screens.Subcomponents.Chip(
                                text = " $skill",
                                onDelete = {
                                    selectedSkills = selectedSkills - skill
                                    if (selectedSkills.isEmpty()) {
                                        selectedSkill = null
                                    }
                                }
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { validateAndSave() },
                shape = RoundedCornerShape(5.dp),
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color(6, 214, 160),
                ),
            ) {
                Text("Upload")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(5.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(239, 71, 111),
                    ),
                ){
                Text("Cancel")
            }
        }
    )
}



@Preview
@Composable
fun PreviewJobModal() {
    var showModal by remember { mutableStateOf(true) }

    if (showModal) {
        JobModal(
            onDismiss = { showModal = false },
            onSave = { education ->
                showModal = false
            }
        )
    }
}
