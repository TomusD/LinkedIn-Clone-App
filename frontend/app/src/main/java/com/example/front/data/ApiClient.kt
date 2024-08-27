package com.example.front.data

import com.example.front.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val IP = BuildConfig.ip
    private const val PORT = BuildConfig.port

    private const val BASE_URL = "http://$IP:$PORT"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}
