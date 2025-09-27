package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Pair<Boolean, UserDTO?> {
        val isLoggedIn = repository.isLoggedIn()
        val user = if (isLoggedIn) repository.getCurrentUser() else null
        return Pair(isLoggedIn, user)
    }
}
