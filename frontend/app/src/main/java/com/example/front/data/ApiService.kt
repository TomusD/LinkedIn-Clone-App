package com.example.front.data

import com.example.front.data.response.Test
import com.example.front.data.response.User
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    fun getMessage() : Call<Test>

    @Headers("Content-Type: application/json")
    @POST("/signup")
    fun signUpUser(@Body user: User): Call<Test>
}