package com.example.front.data

import com.example.front.data.request.UserLoginRequest
import com.example.front.data.response.Test
import com.example.front.data.request.UserRegister
import com.example.front.data.response.UserLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/token")
    fun signInUser(@Body user: UserLoginRequest): Call<UserLoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/users")
    fun signUpUser(@Body user: UserRegister): Call<UserRegister>
}