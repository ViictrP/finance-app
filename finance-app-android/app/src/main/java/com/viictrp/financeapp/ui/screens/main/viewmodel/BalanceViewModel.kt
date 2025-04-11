package com.viictrp.financeapp.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.service.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _balance = MutableStateFlow<BalanceDTO?>(null)
    val balance = _balance

    private val _currentBalance = MutableStateFlow<BalanceDTO?>(null)
    val currentBalance = _currentBalance

    suspend fun loadBalance(yearMonth: YearMonth, defineCurrent: Boolean = false) {
        _balance.value = apiService.getBalance(yearMonth)
        if (defineCurrent) {
            setCurrentBalance(balance.value)
        }
    }

    fun setCurrentBalance(balance: BalanceDTO?) {
        _currentBalance.value = balance
    }
}