package com.example.front.data

import com.example.front.data.request.UserLoginRequest
import com.example.front.data.response.Test
import com.example.front.data.request.UserRegister
import com.example.front.data.response.APIResponse
import com.example.front.data.response.Token
import com.example.front.data.response.UserLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/token")
    fun signInUser(@Body user: UserLoginRequest): Call<UserLoginResponse>

    @Multipart
    @POST("/users")
    fun signUpUser(
        @Part("nameBody") name: RequestBody,
        @Part("surnameBody") surname: RequestBody,
        @Part("emailBody") email: RequestBody,
        @Part("passwordBody") password: RequestBody,
        @Part image: MultipartBody.Part? = null,
    ): Call<APIResponse>
}