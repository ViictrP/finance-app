package com.viictrp.financeapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.data.remote.dto.UserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    private val _loading = MutableStateFlow(true)

    private val _user = MutableStateFlow<UserDTO?>(null)
    val user = _user

    fun checkAuth() {
        viewModelScope.launch {
            _loading.value = true
            _isAuthenticated.value = authManager.isLoggedIn()

            if (isAuthenticated.value == true) {
                _user.value = authManager.user
            }
            _loading.value = false
        }
    }

    fun loginWithGoogle(onResult: (message: String?) -> Unit) {
        viewModelScope.launch {
            authManager.signInWithGoogle { user, message ->
                _isAuthenticated.value = true
                _user.value = user
                onResult(message)
            }
        }
    }

    fun resetAuth() {
        _isAuthenticated.update {
            authManager.clearTokens()
            false
        }
    }

    fun logout() {
        authManager.clearTokens()
        _isAuthenticated.value = false
    }
}