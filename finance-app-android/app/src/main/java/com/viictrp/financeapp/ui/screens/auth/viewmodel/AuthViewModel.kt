package com.viictrp.financeapp.ui.screens.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.application.dto.UserDTO
import com.viictrp.financeapp.ui.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    val loading: StateFlow<Boolean> = _loading

    private val _user = MutableLiveData<UserDTO?>(null)
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