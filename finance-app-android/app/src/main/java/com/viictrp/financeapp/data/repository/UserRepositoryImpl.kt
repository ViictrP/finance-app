package com.viictrp.financeapp.data.repository

import com.viictrp.financeapp.data.local.dao.UserDao
import com.viictrp.financeapp.data.local.entity.UserEntity
import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun saveUser(user: UserDTO) {
        val userEntity = UserEntity(
            id = user.email, // Usando email como ID único
            name = user.fullName,
            email = user.email,
            photoUrl = user.pictureUrl
        )
        userDao.saveUser(userEntity)
    }

    override suspend fun getUser(): UserDTO? {
        return userDao.getUser()?.let { entity ->
            UserDTO(
                email = entity.email,
                fullName = entity.name,
                pictureUrl = entity.photoUrl ?: "",
                accessToken = "" // Token não é persistido por segurança
            )
        }
    }

    override suspend fun clearUser() {
        userDao.clearUser()
    }
}
