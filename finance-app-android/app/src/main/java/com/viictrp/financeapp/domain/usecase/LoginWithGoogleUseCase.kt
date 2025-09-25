package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(onResult: (user: UserDTO?, message: String?) -> Unit) {
        repository.signInWithGoogle(onResult)
    }
}
