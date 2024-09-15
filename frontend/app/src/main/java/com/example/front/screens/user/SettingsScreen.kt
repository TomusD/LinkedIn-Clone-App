package com.example.front.screens.user

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.MutableLiveData
import com.example.front.data.ApiClient
import com.example.front.data.request.UserSettings
import com.example.front.data.response.APIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val serverError = MutableLiveData<String?>(null)

    fun saveSettings(context: Context, oldPwd: String, newPwd: String? = null, newEmail: String? = null) {
        val apiClient = ApiClient()

        val settings = UserSettings(oldPwd, newPwd, newEmail)
        val call = apiClient.getApiService(context).updateSettings(settings)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "SETTINGS - SUCCESS: ${response.body()}")
                } else {
                    Log.d("MYTEST", "SETTINGS - NOT SUCCESS: ${response.body()}")
                    serverError.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("MYTEST", "SETTINGS - FAILURE: "+ t.message.toString())
                serverError.value = t.message.toString()
            }
        })

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    val activity = (context as? Activity)
                    activity?.finish()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var showNewPasswordFields by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }


        TextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("Old Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password (Optional") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(17, 138, 178),
                focusedTextColor = Color.Black,
                cursorColor = Color.Black),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New Email
        TextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            label = { Text("New Email (Optional)") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

            Spacer(modifier = Modifier.height(16.dp))

            // Show validation error message if any
            errorMessage?.let {
                Text(text = it, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Save button
            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(17, 138, 178)),
                onClick = {
                    // Validation logic before calling the callback
                    if (oldPassword.isEmpty()) {
                        errorMessage = "Current password is required"
                    } else if (showNewPasswordFields && newPassword != confirmPassword) {
                        errorMessage = "Passwords do not match"
                    } else if (showNewPasswordFields && newPassword.length < 6) {
                        errorMessage = "New password must be at least 6 characters long"
                    } else {
                        errorMessage = null
                        saveSettings(context, oldPassword, newPassword, newEmail)
                        errorMessage = serverError.value
                        if (errorMessage.isNullOrEmpty()) {
                            Toast.makeText(context, "Settings updated!", Toast.LENGTH_SHORT)
                        }

                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }

}
