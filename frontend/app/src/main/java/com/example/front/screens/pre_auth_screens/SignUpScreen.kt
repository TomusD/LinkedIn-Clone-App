package com.example.front.screens.pre_auth_screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.front.R
import com.example.front.data.ApiClient
import com.example.front.data.response.Test
import com.example.front.ui.theme.Unna

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(
            onClick = {navController.navigateUp()}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back"
            )
        }

        Text(
            modifier = Modifier.width(250.dp),
            text = stringResource(R.string.create_new_account),
            fontSize = 40.sp,
            fontFamily = Unna,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.em
        )

        TextField(
            value = name, onValueChange = { newText ->
                name = newText
            },
            label = {
                Text(text = stringResource(R.string.enter_name_label))
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_name))
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )

        var email by remember { mutableStateOf("") }
        TextField(
            value = email,
            onValueChange = { newText ->
                email = newText
            },
            label = {
                Text(text = stringResource(R.string.enter_email_label))
            },
            placeholder = {
                Text(text = stringResource(R.string.enter_email))
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),

            )

        var pass by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        TextField(
            value = pass,
            onValueChange = { newText ->
                pass = newText
            },
            label = {
                Text(text = stringResource(R.string.enter_password_label))
            },
            placeholder = {
                Text(text = stringResource(R.string.create_password))
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Face
                else Icons.Filled.Favorite

                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, contentDescription = "")
                }
            },
        )


        var verify_pass by remember { mutableStateOf("") }
        TextField(
            value = verify_pass,
            onValueChange = { newText -> verify_pass = newText },
            label = {Text(text = stringResource(R.string.verify_password_label)) },
            singleLine = true,
            isError = true,
            supportingText = { Text(text = "Verify your password")},
            placeholder = {Text(text = stringResource(R.string.verify_password)) },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Face
                else Icons.Filled.Favorite

                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, contentDescription = "")
                }
            },
        )

        Button(
            onClick = {
                val call = ApiClient.apiService.getMessage()

                call.enqueue(object : Callback<Test> {
                    override fun onResponse(call: Call<Test>, response: Response<Test>) {
                        if (response.isSuccessful) {
                            val res = response.body()
                            val d = Log.d("TEST", res.toString())
                        } else {
                            Log.d("TEST", "RESPONSE NOT SUCCESFULL")
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<Test>, t: Throwable) {
                        Log.d("TEST", "FAILURE: "+ t.message.toString())

                    }
                }
                )
            },
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(17, 138, 178),
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.sign_up))
        }

    }
}