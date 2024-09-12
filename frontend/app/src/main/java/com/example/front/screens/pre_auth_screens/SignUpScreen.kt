package com.example.front.screens.pre_auth_screens

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.front.R
import com.example.front.data.ApiClient
import com.example.front.data.request.UserRegister
import com.example.front.ui.theme.Unna
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import com.example.front.activity.fileToMedia
import com.example.front.data.response.APIResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: RegisterViewModel= viewModel()) {
    val context = LocalContext.current

    val isLoading by viewModel.isLoading.observeAsState(false)

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var verifyPwd by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start)
        {
            IconButton(
                onClick = { navController.navigateUp() }) {
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
                value = name, onValueChange = { newText -> name = newText },
                label = { Text(text = stringResource(R.string.enter_name_label)) },
                placeholder = { Text(text = stringResource(R.string.enter_name)) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
            )

            TextField(
                value = surname, onValueChange = { newText -> surname = newText },
                label = { Text(text = stringResource(R.string.enter_surname_label)) },
                placeholder = { Text(text = stringResource(R.string.enter_surname)) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
            )

            TextField(
                value = email, onValueChange = { newText -> email = newText },
                label = { Text(text = stringResource(R.string.enter_email_label)) },
                placeholder = { Text(text = stringResource(R.string.enter_email)) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )


            TextField(
                value = pass, onValueChange = { newText -> pass = newText },
                label = { Text(text = stringResource(R.string.enter_password_label)) },
                placeholder = { Text(text = stringResource(R.string.create_password)) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Face
                    else Icons.Filled.Favorite

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "")
                    }
                },
            )

            TextField(
                value = verifyPwd,
                onValueChange = { newText -> verifyPwd = newText },
                label = { Text(text = stringResource(R.string.verify_password_label)) },
                singleLine = true,
                placeholder = { Text(text = stringResource(R.string.verify_password)) },
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Face
                    else Icons.Filled.Favorite

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "")
                    }
                },
            )

            Row(Modifier.fillMaxHeight()) {

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Upload picture")
                }

                imageUri?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(it).build(),
                        contentDescription = "Selected Image",
                        modifier = Modifier.size(50.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val newUser = UserRegister(name, surname, email, pass, imageUri)
                    viewModel.uploadUserRegistration(newUser, context)
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

        // Show a CircularProgressIndicator on top of the form when loading
        LoadingIndicator(viewModel.isLoading)

    }
}

@Composable
fun LoadingIndicator(isLoading: LiveData<Boolean>) {
    // Observe LiveData as state
    val isLoadingState by isLoading.observeAsState(false)

    if (isLoadingState) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp) // Adjust size as needed
            )
        }
    }
}


class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val isSuccessful = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String?>()

    fun uploadUserRegistration(user: UserRegister, context: Context) {
        _isLoading.value = true

        val nameBody = user.name.toRequestBody("text/plain".toMediaTypeOrNull())
        val surnameBody = user.surname.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = user.email.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordBody = user.password.toRequestBody("text/plain".toMediaTypeOrNull())


        val imageBody =  user.imageURI?.let { fileToMedia(context, it, "image") }
        if (imageBody != null) {


            viewModelScope.launch {

                try {
                    val apiClient = ApiClient()
                    val call = apiClient.getApiService(context).signUpUser(
                        nameBody,
                        surnameBody,
                        emailBody,
                        passwordBody,
                        imageBody
                    )
                    call.enqueue(object : Callback<APIResponse> {
                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            if (response.isSuccessful) {
                                val res = response.body()
                                Log.d("MYTEST", res.toString())
                                _isLoading.value = false

                            } else {
                                _isLoading.value = false
                                Log.e("MYTEST", "RESPONSE NOT SUCCESSFUL")
                            }
                        }

                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            _isLoading.value = false
                            Log.e("MYTEST", "FAILURE: "+ t.message.toString())

                        }
                    })

                } catch (e: Exception) {
                    // Handle network error
                    isSuccessful.value = false
                    _isLoading.value = false

                    errorMessage.value = "Error: ${e.message}"
                    Log.e("MYTEST", e.toString())

                } finally {
//                    _isLoading.value = false
                }
            }

        } else {
            Log.e("MYTEST", "FAILED TO GET REAL LIFE PATH FROM URI")
            _isLoading.value = false
        }

    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "unknown_file"
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName
}
