package com.viictrp.financeapp.data.repository

import com.viictrp.financeapp.auth.AuthManager
import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }

    override suspend fun getCurrentUser(): UserDTO? {
        return authManager.user
    }

    override suspend fun signInWithGoogle(onResult: (user: UserDTO?, message: String?) -> Unit) {
        authManager.signInWithGoogle(onResult)
    }

    override suspend fun logout() {
        authManager.clearTokens()
    }

    override suspend fun resetAuth() {
        authManager.clearTokens()
    }
}
