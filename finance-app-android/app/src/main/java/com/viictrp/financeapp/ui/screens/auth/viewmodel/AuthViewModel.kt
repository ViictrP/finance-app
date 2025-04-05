package com.viictrp.financeapp.ui.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.ui.auth.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun checkAuth() {
        viewModelScope.launch {
            _loading.value = true
            _isAuthenticated.value = authManager.isLoggedIn()
            _loading.value = false
        }
    }

    fun login(onSuccess: () -> Unit) {
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