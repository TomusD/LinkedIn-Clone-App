package com.example.front.screens.Subcomponents.modals

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import com.example.front.data.ApiClient
import com.example.front.data.request.Work
import com.example.front.data.response.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkModal(
    onDismiss: () -> Unit,
    onSave: (Work) -> Unit
) {

    val context = LocalContext.current

    var organization by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var dateStarted by remember { mutableStateOf("") }
    var dateEnded by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun updateWork(work: Work) {
        val apiClient = ApiClient()

        Log.d("MYTEST", work.toString())
        val call = apiClient.getApiService(context).updateWork(work)

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
            val startDate = LocalDate.parse(dateStarted, formatter)
            val endDate = if (dateEnded.isNotEmpty()) LocalDate.parse(dateEnded, formatter) else null

            if (organization.isBlank() || role.isBlank()) {
                errorMessage = "Please fill all required fields."
            } else {
                errorMessage = null

                val work: Work = Work(organization, role, startDate.toString(), endDate?.toString())

                updateWork(work)
                Toast.makeText(context, "Re-visit to see the updated list", Toast.LENGTH_SHORT).show()
                onSave(work)
            }
        } catch (e: NumberFormatException) {
            errorMessage = "Degree must be a number."
        } catch (e: DateTimeParseException) {
            errorMessage = "Date format must be yyyy-MM-dd."
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(5.dp),
        title = { Text(text = "+ Add Work Experience") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

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
                    value = dateStarted, onValueChange = { dateStarted = it },
                    label = { Text(text = "Date Started (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Date Started (yyyy-MM-dd)") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = dateEnded, onValueChange = { dateEnded = it },
                    label = { Text(text = "Date Ended (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(text = "Date Ended (yyyy-MM-dd)") },
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.LightGray)
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
                Text("Save")
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
fun PreviewWorkModal() {
    var showModal by remember { mutableStateOf(true) }

    if (showModal) {
        WorkModal(
            onDismiss = { showModal = false },
            onSave = { education ->
                println("Saved Education: $education")
                showModal = false
            }
        )
    }
}
