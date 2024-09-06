package com.example.front.data

import com.example.front.data.request.Work
import com.example.front.data.response.APIResponse
import com.example.front.data.response.auth.LoginResponse
import com.example.front.data.response.UsersList
import com.example.front.data.response.WorkList
import com.example.front.data.response.WorkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
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

    @Headers("Content-Type: application/json")
    @POST("/profile/work")
    fun updateWork(@Body work: Work): Call<APIResponse>

//    @Headers("Content-Type: application/json")
    @GET("/profile/work/me")
    fun getWorkExperience(): Call<WorkList>

}