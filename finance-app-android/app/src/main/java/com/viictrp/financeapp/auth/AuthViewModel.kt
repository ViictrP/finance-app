package com.viictrp.financeapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.usecase.CheckAuthUseCase
import com.viictrp.financeapp.domain.usecase.LoginWithGoogleUseCase
import com.viictrp.financeapp.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _user = MutableStateFlow<UserDTO?>(null)
    val user: StateFlow<UserDTO?> = _user

    fun checkAuth() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val (isLoggedIn, user) = checkAuthUseCase()
                _isAuthenticated.value = isLoggedIn
                _user.value = user
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _user.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    fun loginWithGoogle(onResult: (message: String?) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                loginWithGoogleUseCase { user, message ->
                    _isAuthenticated.value = user != null
                    _user.value = user
                    _loading.value = false
                    onResult(message)
                }
            } catch (e: Exception) {
                _loading.value = false
                onResult("Erro durante o login")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _isAuthenticated.value = false
                _user.value = null
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun resetAuth() {
        _isAuthenticated.value = null
        _user.value = null
        _loading.value = true
    }
}
