package com.viictrp.financeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.service.ApiService
import kotlinx.coroutines.launch

class BalanceViewModel(private val apiService: ApiService) : ViewModel() {

    private val _balance = MutableLiveData<BalanceDTO?>()
    val balance: LiveData<BalanceDTO?> = _balance

    fun loadBalance() {
        viewModelScope.launch {
            _balance.value = apiService.getBalance()
        }
    }
}