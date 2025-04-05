package com.viictrp.financeapp.ui.auth

import android.content.Context
import androidx.core.content.edit

class AuthManager(context: Context) {

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value) = prefs.edit() { putString("access_token", value) }

    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value) = prefs.edit() { putString("refresh_token", value) }

    fun clearTokens() {
        prefs.edit() { clear() }
    }

    fun isLoggedIn(): Boolean = !accessToken.isNullOrBlank()

    suspend fun logIn(onSuccess: () -> Unit) {
        onSuccess()
    }

    suspend fun refreshTokenIfNeeded(): Boolean {
        // check if expired or about to expire
        // call API to refresh
        // update tokens if successful
        return true // or false if it fails
    }
}