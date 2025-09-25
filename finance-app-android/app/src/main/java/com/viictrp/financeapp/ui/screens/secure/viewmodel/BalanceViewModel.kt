package com.viictrp.financeapp.ui.screens.secure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.InvoiceDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.usecase.DeleteTransactionUseCase
import com.viictrp.financeapp.domain.usecase.GetCurrentBalanceUseCase
import com.viictrp.financeapp.domain.usecase.GetInvoiceUseCase
import com.viictrp.financeapp.domain.usecase.LoadInstallmentsUseCase
import com.viictrp.financeapp.domain.usecase.SaveCreditCardUseCase
import com.viictrp.financeapp.domain.usecase.SaveTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val getCurrentBalanceUseCase: GetCurrentBalanceUseCase,
    private val getInvoiceUseCase: GetInvoiceUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val saveCreditCardUseCase: SaveCreditCardUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val loadInstallmentsUseCase: LoadInstallmentsUseCase
) : ViewModel() {

    private val _lastUpdateTime = MutableStateFlow<Instant?>(null)
    val lastUpdateTime: StateFlow<Instant?> = _lastUpdateTime

    private val _balance = MutableStateFlow<BalanceDTO?>(null)
    val balance: StateFlow<BalanceDTO?> = _balance

    private val _selectedInvoice = MutableStateFlow<InvoiceDTO?>(null)
    val selectedInvoice: StateFlow<InvoiceDTO?> = _selectedInvoice

    private val _selectedCreditCard = MutableStateFlow<CreditCardDTO?>(null)
    val selectedCreditCard: StateFlow<CreditCardDTO?> = _selectedCreditCard

    private val _selectedTransaction = MutableStateFlow<TransactionDTO?>(null)
    val selectedTransaction: StateFlow<TransactionDTO?> = _selectedTransaction

    private val _currentBalance = MutableStateFlow<BalanceDTO?>(null)
    val currentBalance: StateFlow<BalanceDTO?> = _currentBalance

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())
    internal val selectedYearMonth: StateFlow<YearMonth> = _selectedYearMonth

    private val _loading = MutableStateFlow(false)
    internal val loading: StateFlow<Boolean> = _loading

    private val _deleteTransactionSuccess = MutableSharedFlow<Unit>()
    val deleteTransactionSuccess: SharedFlow<Unit> = _deleteTransactionSuccess.asSharedFlow()

    private val _creditCards = MutableStateFlow<List<CreditCardDTO>>(emptyList())
    val creditCards: StateFlow<List<CreditCardDTO>> = _creditCards.asStateFlow()

    suspend fun loadBalance(yearMonth: YearMonth, defineCurrent: Boolean = false) {
        try {
            _loading.value = true
            val balance = getCurrentBalanceUseCase(yearMonth)
            _balance.value = balance
            if (defineCurrent) {
                _currentBalance.value = balance
            }
            _lastUpdateTime.value = Instant.now()
        } catch (e: Exception) {
            // Handle error
        } finally {
            _loading.value = false
        }
    }

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth) {
        try {
            _loading.value = true
            val invoice = getInvoiceUseCase(creditCardId, yearMonth)
            _selectedInvoice.value = invoice
        } catch (e: Exception) {
            // Handle error
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveCreditCardTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        _loading.value = true
        return try {
            saveTransactionUseCase(newTransactionDTO)
        } catch (e: Exception) {
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        _loading.value = true
        return try {
            saveTransactionUseCase(newTransactionDTO)
        } catch (e: Exception) {
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun saveCreditCard(newCreditCardDTO: CreditCardDTO): CreditCardDTO? {
        _loading.value = true
        return try {
            saveCreditCardUseCase(newCreditCardDTO)
        } catch (e: Exception) {
            null
        } finally {
            _loading.value = false
        }
    }

    suspend fun loadInstallments(installmentId: String): List<TransactionDTO?> {
        return loadInstallmentsUseCase(installmentId)
    }

    fun deleteTransaction(id: Long, all: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            try {
                deleteTransactionUseCase(id, all)
                _deleteTransactionSuccess.emit(Unit)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _loading.value = false
            }
        }
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
    }
}
