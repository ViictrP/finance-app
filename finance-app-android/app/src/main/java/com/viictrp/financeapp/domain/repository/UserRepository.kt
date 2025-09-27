package com.viictrp.financeapp.domain.repository

import com.viictrp.financeapp.data.remote.dto.UserDTO

interface UserRepository {
    suspend fun saveUser(user: UserDTO)
    suspend fun getUser(): UserDTO?
    suspend fun clearUser()
}
