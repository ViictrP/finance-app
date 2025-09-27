package com.viictrp.financeapp.domain.repository

import com.viictrp.financeapp.data.remote.dto.UserDTO

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun getCurrentUser(): UserDTO?
    suspend fun signInWithGoogle(onResult: (user: UserDTO?, message: String?) -> Unit)
    suspend fun logout()
    suspend fun resetAuth()
}
