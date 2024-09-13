package com.example.front.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.request.CommentCreate
import com.example.front.data.response.APIResponse
import com.example.front.data.response.Comments
import com.example.front.data.response.PostResponse
import com.example.front.data.response.PostsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _posts = MutableStateFlow<List<PostResponse>>(mutableListOf())
    val posts: StateFlow<List<PostResponse>> get() = _posts

    fun fetchPosts(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getPosts()

        call.enqueue(object : Callback<PostsList> {
            override fun onResponse(call: Call<PostsList>, response: Response<PostsList>) {
                if (response.isSuccessful) {
                    _posts.value = response.body()?.posts ?: emptyList()

                    Log.d("MYTEST", "FEED GET --- $response")

                } else {
                    _isLoading.value = false
                    val res = response.body()
                    Log.e("MYTEST", "FEED GET --- RESPONSE NOT SUCCESSFUL - $res")
                }
            }

            override fun onFailure(p0: Call<PostsList>, p1: Throwable) {
                Log.e("MYTEST", "FEED GET --- FAILURE - ${p1.message}")
            }
        })
    }

    fun postComment(context: Context, post_id: Int, com: CommentCreate) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).commentPost(post_id, com)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "COMMENT POST GET --- ${response.body()}")

                } else {
                    _isLoading.value = false
                    val res = response.body()
                    Log.e("MYTEST", "COMMENT POST GET --- RESPONSE NOT SUCCESSFUL - $res")
                }
            }

            override fun onFailure(p0: Call<APIResponse>, p1: Throwable) {
                Log.e("MYTEST", "COMMENT POST GET --- FAILURE - ${p1.message}")
            }
        })
    }

    fun updateLike(post_id: Int) {
        _posts.update {
            it.map { post ->
                if (post.post_id == post_id) {
                    post.copy(
                        user_liked = !post.user_liked,
                        likes = post.likes + if (!post.user_liked) 1 else -1
                    )

                } else {
                    post
                }
            }
        }
    }

    fun updateComment(new_comment: Comments, post_id: Int) {
        _posts.update {
            it.map { post ->
                if (post.post_id == post_id) {
                    post.comments.add(0, new_comment)
                    post.copy(
                        comments = post.comments,
                    )

                } else {
                    post
                }
            }
        }
    }

    fun likePost(context: Context, post_id: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).likePost(post_id)

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "LIKE POST GET --- ${response.body()}")

                } else {
                    _isLoading.value = false
                    val res = response.body()
                    Log.e("MYTEST", "LIKE POST GET --- RESPONSE NOT SUCCESSFUL - $res")
                }
            }

            override fun onFailure(p0: Call<APIResponse>, p1: Throwable) {
                Log.e("MYTEST", "LIKE POST GET --- FAILURE - ${p1.message}")
            }
        })
    }
}

fun formatDateTime(input: String): String {
    // Define the input date-time format
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    // Parse the input string into a LocalDateTime object
    val dateTime = LocalDateTime.parse(input, inputFormatter)

    // Define the output date-time format
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")

    // Format the LocalDateTime object to the desired string format
    return dateTime.format(outputFormatter)
}