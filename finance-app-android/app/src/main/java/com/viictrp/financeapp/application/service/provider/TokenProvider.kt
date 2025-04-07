package com.viictrp.financeapp.application.service.provider

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object TokenProvider {
    suspend fun getToken(forceRefresh: Boolean = false): String? {
        return try {
            FirebaseAuth.getInstance().currentUser
                ?.getIdToken(forceRefresh)
                ?.await()
                ?.token
        } catch (e: Exception) {
            Log.d("TokenProvider", "Error getting token: ${e.message}")
            null
        }
    }
}