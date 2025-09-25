package com.viictrp.financeapp.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.data.remote.dto.UserDTO
import com.viictrp.financeapp.domain.usecase.CheckAuthUseCase
import com.viictrp.financeapp.domain.usecase.LoginWithGoogleUseCase
import com.viictrp.financeapp.domain.repository.UserRepository
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
    private val logoutUseCase: LogoutUseCase,
    private val userRepository: UserRepository
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
                // Primeiro tenta carregar dados locais
                val localUser = userRepository.getUser()
                if (localUser != null) {
                    _user.value = localUser
                    _isAuthenticated.value = true
                }
                
                // Depois verifica com o servidor
                val (isLoggedIn, serverUser) = checkAuthUseCase()
                _isAuthenticated.value = isLoggedIn
                
                if (isLoggedIn && serverUser != null) {
                    _user.value = serverUser
                    // Salva/atualiza dados locais
                    userRepository.saveUser(serverUser)
                } else if (!isLoggedIn) {
                    // Se não está logado, limpa dados locais
                    userRepository.clearUser()
                    _user.value = null
                }
            } catch (e: Exception) {
                // Em caso de erro, usa dados locais se disponíveis
                val localUser = userRepository.getUser()
                if (localUser != null) {
                    _user.value = localUser
                    _isAuthenticated.value = true
                } else {
                    _isAuthenticated.value = false
                    _user.value = null
                }
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
                    
                    // Salva dados locais se login foi bem-sucedido
                    if (user != null) {
                        viewModelScope.launch {
                            userRepository.saveUser(user)
                        }
                    }
                    
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
                userRepository.clearUser()
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
