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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.front.data.SessionManager
import com.example.front.data.request.UserLoginRequest
import com.example.front.data.request.UserRegister
import com.example.front.data.response.Test
import com.example.front.data.response.UserLoginResponse
import com.example.front.ui.theme.Unna
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start,
    ) {
        IconButton(
            onClick = {navController.navigateUp()}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back"
            )
        }

        Text(
            modifier = Modifier.width(200.dp),
            text = stringResource(R.string.signin_message),
            fontSize = 40.sp,
            fontFamily = Unna,
            fontWeight = FontWeight.Bold,
            lineHeight = 1.em
        )

        TextField(
            value = email,
            onValueChange = { newText -> email = newText },
            label = { Text(text = stringResource(R.string.enter_email_label)) },
            placeholder = { Text(text = stringResource(R.string.enter_email)) },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
        )

        TextField(
            value = pass,
            onValueChange = { newText -> pass = newText },
            label = { Text(text = stringResource(R.string.enter_password_label)) },
            placeholder = { Text(text = stringResource(R.string.enter_password)) },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.FavoriteBorder
                else Icons.Filled.Favorite

                IconButton(onClick = {passwordVisible = !passwordVisible }) { Icon(imageVector = image, contentDescription = "") }
            }
        )

        Button(
            onClick = {
                var apiClient = ApiClient()

                val user = UserLoginRequest(email, pass)
                val call = apiClient.getApiService(context).signInUser(user)

                call.enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                        if (response.isSuccessful) {
                            val res = response.body()
                            Log.d("MYTEST", res.toString())
                            onLoginSuccess()
                        } else {
                            Log.d("MYTEST", "RESPONSE NOT SUCCESSFUL")
                            // Handle error
                        }
                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        Log.d("MYTEST", "FAILURE: "+ t.message.toString())

                    }
                })
            },
            Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(17, 138, 178),
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.sign_in), fontWeight = FontWeight.Bold)
        }

    }

}