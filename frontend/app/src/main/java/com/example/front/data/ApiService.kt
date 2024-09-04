package com.example.front.data

import com.example.front.data.response.APIResponse
import com.example.front.data.response.LoginResponse
import com.example.front.data.response.UsersList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("/token")
    fun signInUser(@Field("username") username: String,
                   @Field("password") password: String,): Call<LoginResponse>

    @Multipart
    @POST("/users")
    fun signUpUser(
        @Part("nameBody") name: RequestBody,
        @Part("surnameBody") surname: RequestBody,
        @Part("emailBody") email: RequestBody,
        @Part("passwordBody") password: RequestBody,
        @Part image: MultipartBody.Part? = null,
    ): Call<APIResponse>


    @GET("/users")
    fun getUsers(): Call<UsersList>

}