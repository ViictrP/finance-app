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
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager
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
            _isAuthenticated.value = authManager.isLoggedIn()

            if (_isAuthenticated.value == true) {
                _user.value = authManager.user
            }
            _loading.value = false
        }
    }

    fun loginWithGoogle(onResult: (message: String?) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            authManager.signInWithGoogle { user, message ->
                // actualize o estado apenas quando o login for bem‑sucedido
                _isAuthenticated.value = user != null
                _user.value = user
                _loading.value = false
                onResult(message)
            }
        }
    }

    fun resetAuth() {
        viewModelScope.launch {
            authManager.clearTokens()
            _isAuthenticated.value = false
            _user.value = null
        }
    }

    fun logout() {
        viewModelScope.launch {
            authManager.clearTokens()
            _isAuthenticated.value = false
            _user.value = null
        }
    }
}
