package com.viictrp.financeapp.ui.screens.viewmodel

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

    private val _selectedInvoice = MutableStateFlow<InvoiceDTO?>(null)
    val selectedInvoice = _selectedInvoice

    private val _selectedCreditCard = MutableStateFlow<CreditCardDTO?>(null)
    val selectedCreditCard = _selectedCreditCard

    private val _selectedTransaction = MutableStateFlow<TransactionDTO?>(null)
    val selectedTransaction = _selectedTransaction

    private val _currentBalance = MutableStateFlow<BalanceDTO?>(null)
    val currentBalance = _currentBalance

    private val _selectedYearMonth = MutableStateFlow<YearMonth>(YearMonth.now())
    internal val selectedYearMonth = _selectedYearMonth

    private val _loading = MutableStateFlow(false)
    internal val loading = _loading

    suspend fun loadBalance(yearMonth: YearMonth, defineCurrent: Boolean = false) {
        try {
            _loading.value = true
            _balance.value = apiService.getBalance(yearMonth)
            if (defineCurrent) {
                setCurrentBalance(balance.value)
                _selectedCreditCard.value = balance.value?.creditCards?.get(0)
            }
        } finally {
            _loading.value = false
        }
    }

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth) {
        try {
            _loading.value = true
            _selectedInvoice.value = apiService.getInvoice(creditCardId, yearMonth)
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveCreditCardTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        _loading.value = true
        return try {
            apiService.saveCreditCardTransaction(newTransactionDTO)
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        _loading.value = true
        return try {
            apiService.saveUserCardTransaction(newTransactionDTO)
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveCreditCard(newCreditCardDTO: CreditCardDTO): CreditCardDTO? {
        _loading.value = true
        return try {
            apiService.saveCreditCard(newCreditCardDTO)
        } finally {
            _loading.value = false
        }
    }

    fun setCurrentBalance(balance: BalanceDTO?) {
        _currentBalance.value = balance
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        _selectedYearMonth.value = yearMonth
    }

    fun selectCreditCard(creditCard: CreditCardDTO) {
        _selectedCreditCard.value = creditCard
        _selectedInvoice.value = creditCard.invoices.getOrNull(0)
    }

    fun selectTransaction(transaction: TransactionDTO, creditCard: CreditCardDTO?) {
        _selectedTransaction.value = transaction
        _selectedCreditCard.value = creditCard
    }

    fun clear() {
        _selectedInvoice.value = null
        _selectedYearMonth.value = YearMonth.now()
        _selectedCreditCard.value = null
        _selectedTransaction.value = null
    }
}