package com.example.front.data

import com.example.front.data.base.Job
import com.example.front.data.base.User
import com.example.front.data.request.CommentCreate
import com.example.front.data.request.Education
import com.example.front.data.request.UserSettings
import com.example.front.data.request.Work
import com.example.front.data.response.APIResponse
import com.example.front.data.response.AllJobs
import com.example.front.data.response.EducationList
import com.example.front.data.response.JobsList
import com.example.front.data.response.PostsList
import com.example.front.data.response.SkillsList
import com.example.front.data.response.UserInfo
import com.example.front.data.response.auth.LoginResponse
import com.example.front.data.response.UsersList
import com.example.front.data.response.WorkList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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

    @GET("/users/")
    fun getUsers(): Call<UsersList>


    @GET("/user/{user_id}")
    fun getUser(@Path("user_id") user_id: Int): Call<User>

    @GET("/friends/profile/{friend_id}")
    fun getFriendInfo(@Path("friend_id") friend_id: Int): Call<UserInfo>

    @PUT("/friends/request/{friend_id}")
    fun addFriend(@Path("friend_id") friend_id: Int): Call<APIResponse>

    @Multipart
    @POST("/posts")
    fun createPost(
        @Part("text_field") text_field: RequestBody,
        @Part media_image: MultipartBody.Part? = null,
        @Part media_video: MultipartBody.Part? = null,
        @Part media_audio: MultipartBody.Part? = null,
    ): Call<APIResponse>

    @GET("/posts")
    fun getPosts(): Call<PostsList>

    @PUT("/posts/{post_id}/like")
    fun likePost(@Path("post_id") post_id: Int): Call<APIResponse>

    @POST("/posts/{post_id}/comment")
    fun commentPost(@Path("post_id") post_id: Int, @Body comment: CommentCreate): Call<APIResponse>

    @PUT("/settings")
    fun updateSettings(@Body userSettings: UserSettings): Call<APIResponse>

    @Headers("Content-Type: application/json")
    @POST("/profile/work")
    fun updateWork(@Body work: Work): Call<APIResponse>

    @GET("/profile/work/me")
    fun getWorkExperience(): Call<WorkList>

    @Headers("Content-Type: application/json")
    @POST("/profile/education")
    fun updateEducation(@Body edu: Education): Call<APIResponse>

    @GET("/profile/edu/me")
    fun getEducation(): Call<EducationList>

    @PUT("/profile/skills")
    fun updateSkills(@Body skills: SkillsList): Call<APIResponse>

    @GET("/profile/skills/{user_id}")
    fun getSkills(@Path("user_id") user_id: Int): Call<SkillsList>

    @GET("/profile/publicity/all/{user_id}")
    fun getPublicity(@Path("user_id") user_id: Int): Call<Map<String, Boolean>>

    @PUT("/profile/publicity/{information}")
    fun updatePublicity(@Path("information") info: String): Call<APIResponse>

    @POST("/jobs")
    fun uploadJob(@Body job: Job): Call<APIResponse>

    @POST("/jobs/{job_id}/apply")
    fun applyJob(@Path("job_id") job_id: Int): Call<APIResponse>

    @DELETE("/jobs/{job_id}/revoke-apply")
    fun revokeApplyJob(@Path("job_id") job_id: Int): Call<APIResponse>

    @GET("/user/jobs/recommended")
    fun getRecommendedJobs(): Call<JobsList>

    @GET("/user/jobs")
    fun getAllJobs(): Call<AllJobs>

    @GET("/skills/available")
    fun getAvailableSkills(): Call<SkillsList>
}