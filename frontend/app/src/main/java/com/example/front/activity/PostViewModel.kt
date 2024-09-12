package com.example.front.activity

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.request.PostCreate
import com.example.front.data.response.APIResponse
import com.example.front.screens.pre_auth_screens.getFileNameFromUri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class PostViewModel : ViewModel() {


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun createPost(context: Context, post: PostCreate) {
        _isLoading.value = true

        val text_field = post.postText.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageBody = post.imageURI?.let { fileToMedia(context, it, "image") }
        val videoBody = post.videoURI?.let { fileToMedia(context, it, "video") }
        val audioBody = post.audioURI?.let { fileToMedia(context, it, "audio") }

        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).createPost(
            text_field, imageBody, videoBody, audioBody
        )

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("MYTEST", "POST CREATE --- " + res.toString())
                    _isLoading.value = false

                } else {
                    _isLoading.value = false
                    val res = response.body()
                    Log.e("MYTEST", "POST CREATE --- RESPONSE NOT SUCCESSFUL - $res")
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MYTEST", "POST CREATE --- FAILURE: "+ t.message.toString())

            }
        })
    }


}

fun fileToMedia(context: Context, fileURI: Uri, type: String): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(fileURI)

    inputStream?.let {
        val requestBody = object : RequestBody() {
            override fun contentType() = contentResolver.getType(fileURI)?.toMediaTypeOrNull()

            override fun writeTo(sink: okio.BufferedSink) {
                inputStream.use { input ->
                    input.copyTo(sink.outputStream())
                }
            }
        }

        val fileName = getFileNameFromUri(context, fileURI)
        return MultipartBody.Part.createFormData("media_$type", fileName, requestBody)
    }

    return null
}