package com.viictrp.financeapp.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.service.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _balance = MutableStateFlow<BalanceDTO?>(null)
    val balance = _balance

    private val _invoice = MutableStateFlow<InvoiceDTO?>(null)
    val invoice = _invoice

    private val _currentBalance = MutableStateFlow<BalanceDTO?>(null)
    val currentBalance = _currentBalance

    private val _selectedYearMonth = MutableStateFlow<YearMonth>(YearMonth.now())
    internal val selectedYearMonth = _selectedYearMonth

    private val _loading = MutableStateFlow(false)
    internal val loading = _loading

    suspend fun loadBalance(yearMonth: YearMonth, defineCurrent: Boolean = false) {
        _loading.value = true
        _balance.value = apiService.getBalance(yearMonth)
        if (defineCurrent) {
            setCurrentBalance(balance.value)
        }
        _loading.value = false
    }

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth) {
        _loading.value = true
        _invoice.value = apiService.getInvoice(creditCardId, yearMonth)
        _loading.value = false
    }

    fun setCurrentBalance(balance: BalanceDTO?) {
        _currentBalance.value = balance
    }

    fun setInvoice(creditCard: CreditCardDTO?) {
        invoice.value = creditCard?.invoices?.getOrNull(0)
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }

    fun setLoading(loading: Boolean) {
        _loading.value = loading
    }
}