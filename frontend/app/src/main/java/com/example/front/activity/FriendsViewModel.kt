package com.example.front.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.front.data.ApiClient
import com.example.front.data.base.ChatPreview
import com.example.front.data.base.User
import com.example.front.data.base.UserMessage
import com.example.front.data.response.APIResponse
import com.example.front.data.response.ChatsList
import com.example.front.data.response.EducationList
import com.example.front.data.response.MessagesList
import com.example.front.data.response.SkillsList
import com.example.front.data.response.UserInfo
import com.example.front.data.response.WorkList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsViewModel: ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    fun getUser(context: Context, userId: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getUser(userId)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    _user.value = response.body()!!
                    Log.d("MYTEST", "FRIEND - SUCCESS === ${_user.value}")
                } else {
                    Log.d("MYTEST", "FRIEND - NO SUCCESS === ${response.body()}")

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("MYTEST", "FRIEND - FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _chats = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chats: StateFlow<List<ChatPreview>> get() = _chats

    fun getChats(context: Context) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getChats()
        call.enqueue(object : Callback<ChatsList> {
            override fun onResponse(call: Call<ChatsList>, response: Response<ChatsList>) {
                if (response.isSuccessful) {
                    _chats.value = response.body()?.chatsPreviews ?: emptyList()
                    Log.d("MYTEST", "CHATS-SUCCESS ${_chats.value}")
                }
            }

            override fun onFailure(call: Call<ChatsList>, t: Throwable) {
                Log.e("MYTEST", "CHATS-FAILURE: "+ t.message.toString())
            }
        })
    }

    private val _userMsg = MutableStateFlow<List<UserMessage>?>(null)
    val userMsg: StateFlow<List<UserMessage>?> get() = _userMsg

    fun getUserMessages(context: Context, userId: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getMessages(userId)
        call.enqueue(object : Callback<MessagesList> {
            override fun onResponse(call: Call<MessagesList>, response: Response<MessagesList>) {
                if (response.isSuccessful) {
                    _userMsg.value = response.body()?.messages
                    Log.d("MYTEST", "FRIEND - SUCCESS === ${_userMsg.value}")
                } else {
                    Log.d("MYTEST", "FRIEND - NO SUCCESS === ${response.body()}")

                }
            }

            override fun onFailure(call: Call<MessagesList>, t: Throwable) {
                Log.e("MYTEST", "FRIEND - FAILURE: "+ t.message.toString())
            }
        })
    }


    private val _userInfo = MutableStateFlow<UserInfo?>(
        UserInfo(WorkList(emptyList()), EducationList(emptyList()),
            SkillsList(emptyList()), false
        )
    )

    val userInfo: StateFlow<UserInfo?> get() = _userInfo

    fun getPublicInfo(context: Context, userId: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).getFriendInfo(userId)
        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    _userInfo.value = response.body()!!
                    Log.d("MYTEST", "FRIEND INFO - SUCCESS === ${_userInfo.value}")
                } else {
                    Log.d("MYTEST", "FRIEND INFO - NO SUCCESS === ${response.body()}")

                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.e("MYTEST", "FRIEND INFO - FAILURE: "+ t.message.toString())
            }
        })
    }


    fun sendFriendRequest(context: Context, userId: Int) {
        val apiClient = ApiClient()
        val call = apiClient.getApiService(context).addFriend(userId)
        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    Log.d("MYTEST", "SEND FRIEND REQUEST - SUCCESS === ${_userInfo.value}")
                } else {
                    Log.d("MYTEST", "SEND FRIEND REQUEST - NO SUCCESS === ${response.body()}")

                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("MYTEST", "SEND FRIEND REQUEST - FAILURE: "+ t.message.toString())
            }
        })
    }

}