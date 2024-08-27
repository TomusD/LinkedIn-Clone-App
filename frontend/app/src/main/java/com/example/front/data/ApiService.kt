package com.example.front.data

import com.example.front.data.response.Test
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/")
    fun getMessage() : Call<Test>
}