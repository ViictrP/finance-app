package com.viictrp.financeapp.ui.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
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
    val selectedInvoice = _invoice

    private val _selectedCreditCard = MutableStateFlow<CreditCardDTO?>(balance.value?.creditCards?.get(0))
    val selectedCreditCard = _selectedCreditCard

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
            _selectedCreditCard.value = balance.value?.creditCards?.get(0)
        }
        _loading.value = false
    }

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth) {
        _loading.value = true
        _invoice.value = apiService.getInvoice(creditCardId, yearMonth)
        _loading.value = false
    }

    suspend fun saveCreditCardTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        _loading.value = true
        val transaction = apiService.saveCreditCardTransaction(newTransactionDTO)
        _loading.value = false
        return transaction
    }

    suspend fun saveCreditCard(newCreditCardDTO: CreditCardDTO): CreditCardDTO? {
        _loading.value = true
        val creditCard = apiService.saveCreditCard(newCreditCardDTO)
        _loading.value = false
        return creditCard
    }

    fun setCurrentBalance(balance: BalanceDTO?) {
        _currentBalance.value = balance
    }

    fun setInvoice(creditCard: CreditCardDTO?) {
        selectedInvoice.value = creditCard?.invoices?.getOrNull(0)
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }

    fun selectCreditCard(creditCardId: Long) {
        _selectedCreditCard.value = _balance.value?.creditCards?.find { it.id == creditCardId }
    }

    fun clear() {
        _invoice.value = null
        _selectedYearMonth.value = YearMonth.now()
        _selectedCreditCard.value = _balance.value?.creditCards?.get(0)
    }
}