package com.example.front.data

import android.content.Context
import android.content.SharedPreferences
import com.example.front.R
import com.example.front.data.base.User

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val USER_SURNAME = "user_surname"
        const val USER_EMAIL = "user_email"
        const val USER_IMAGE_URL = "user_image_url"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Function to save auth token
     */
    fun saveUserInfo(user: User) {
        val editor = prefs.edit()
        editor.putString(USER_ID, user.id)
        editor.putString(USER_NAME, user.name)
        editor.putString(USER_SURNAME, user.surname)
        editor.putString(USER_EMAIL, user.email)
        editor.putString(USER_IMAGE_URL, user.imagePath)
        editor.apply()
    }
}