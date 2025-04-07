package com.viictrp.financeapp.ui.screens.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.service.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _balance = MutableLiveData<BalanceDTO?>()
    val balance: LiveData<BalanceDTO?> = _balance

    fun loadBalance(yearMonth: YearMonth) {
        viewModelScope.launch {
            _balance.value = apiService.getBalance(yearMonth)
        }
    }
}