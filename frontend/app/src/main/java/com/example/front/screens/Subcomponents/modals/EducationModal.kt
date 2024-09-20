package com.example.front.screens.Subcomponents.modals

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.data.ApiClient
import com.example.front.data.request.Education
import com.example.front.data.response.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun EducationModal(
    onDismiss: () -> Unit,
    onSave: (Education) -> Unit
) {
    val context = LocalContext.current

    var organization by remember { mutableStateOf("") }
    var science_field by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var dateStarted by remember { mutableStateOf("") }
    var dateEnded by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun updateWork(education: Education) {
        val apiClient = ApiClient()
        Log.d("MYTEST", education.toString())
        val call = apiClient.getApiService(context).updateEducation(education)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("MYTEST", "SUCCESS" + res.toString())
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
            val degreeValue = if (degree.isNotEmpty()) degree.toFloat() else null
            val startDate = LocalDate.parse(dateStarted, formatter)
            val endDate = if (dateEnded.isNotEmpty()) LocalDate.parse(dateEnded, formatter) else null

            if (organization.isBlank() || science_field.isBlank()) {
                errorMessage = "Please fill all required fields."
            } else {
                errorMessage = null

                val education = Education(organization, science_field, degreeValue, startDate.toString(), endDate?.toString())
                updateWork(education)
                Toast.makeText(context, "Education added!", Toast.LENGTH_SHORT).show()
                onSave(Education(organization, science_field, degreeValue, dateStarted, dateEnded))
            }
        } catch (e: DateTimeParseException) {
            errorMessage = "Date format must be yyyy-MM-dd."
        }


    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(5.dp),
        title = { Text(text = "+ Add Education") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                TextField(
                    value = organization, onValueChange = { organization = it },
                    label = { Text(text = "Organization") },
                    placeholder = { Text(text = "Organization") },
                    colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = science_field, onValueChange = { science_field = it },
                    label = { Text(text = "Science Field") },
                    placeholder = { Text(text = "Science Field") },
                    colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = degree, onValueChange = { degree = it },
                    label = { Text(text = "Degree") },
                    placeholder = { Text(text = "Degree") },
                    colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = dateStarted, onValueChange = { dateStarted = it },
                    label = { Text(text = "Date Started (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Date Started (yyyy-MM-dd)") },
                    colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = dateEnded, onValueChange = { dateEnded = it },
                    label = { Text(text = "Date Ended (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Date Ended (yyyy-MM-dd)") },
                    colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))
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

                Text("Save", )
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
fun PreviewEducationModal() {
    var showModal by remember { mutableStateOf(true) }

    if (showModal) {
        EducationModal(
            onDismiss = { showModal = false },
            onSave = { education ->
                println("Saved Education: $education")
                showModal = false
            }
        )
    }
}
