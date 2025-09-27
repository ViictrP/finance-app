package com.viictrp.financeapp.ui.screens.secure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.usecase.DeleteTransactionWithCacheUseCase
import com.viictrp.financeapp.domain.usecase.GetCurrentBalanceUseCase
import com.viictrp.financeapp.domain.usecase.GetInvoiceUseCase
import com.viictrp.financeapp.domain.usecase.LoadInstallmentsUseCase
import com.viictrp.financeapp.domain.usecase.SaveCreditCardWithCacheUseCase
import com.viictrp.financeapp.domain.usecase.SaveTransactionWithCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private val saveTransactionWithCacheUseCase: SaveTransactionWithCacheUseCase,
    private val saveCreditCardWithCacheUseCase: SaveCreditCardWithCacheUseCase,
    private val deleteTransactionWithCacheUseCase: DeleteTransactionWithCacheUseCase,
    private val loadInstallmentsUseCase: LoadInstallmentsUseCase
) : ViewModel() {

    // ✅ FULL MVI - Uma única fonte de verdade
    private val _state = MutableStateFlow(BalanceState())
    val state: StateFlow<BalanceState> = _state.asStateFlow()

    // ✅ Eventos únicos (não fazem parte do state)
    private val _deleteTransactionSuccess = MutableSharedFlow<Unit>()
    val deleteTransactionSuccess: SharedFlow<Unit> = _deleteTransactionSuccess.asSharedFlow()

    init {
        handleIntent(BalanceIntent.LoadBalance(YearMonth.now(), defineCurrent = true))
    }

    // ✅ MVI Intent Handler
    fun handleIntent(intent: BalanceIntent) {
        when (intent) {
            is BalanceIntent.LoadBalance -> loadBalance(intent.yearMonth, intent.defineCurrent)
            is BalanceIntent.RefreshBalance -> loadBalance(_state.value.selectedYearMonth, defineCurrent = true)
            is BalanceIntent.UpdateYearMonth -> updateYearMonth(intent.yearMonth)
            is BalanceIntent.LoadInvoice -> loadInvoice(intent.creditCardId, intent.yearMonth)
            is BalanceIntent.SelectTransaction -> selectTransactionById(intent.transactionId)
            is BalanceIntent.SaveTransaction -> saveTransaction(intent.transaction)
            is BalanceIntent.SaveCreditCard -> saveCreditCard(intent.creditCard)
            is BalanceIntent.DeleteTransaction -> deleteTransaction(intent.id, intent.all)
            is BalanceIntent.LoadInstallments -> loadInstallments(intent.clickedTransactionId, intent.installmentId)
            is BalanceIntent.ClearCache -> { /* implementar se necessário */ }
            is BalanceIntent.Clear -> clear()
            is BalanceIntent.ClearInstallmentsArray -> clearInstallments()
        }
    }

    private fun loadBalance(yearMonth: YearMonth, defineCurrent: Boolean = false) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true, error = null)
                
                val startTime = System.currentTimeMillis()
                val balance = getCurrentBalanceUseCase(yearMonth)
                
                _state.value = _state.value.copy(
                    balance = balance,
                    currentBalance = if (defineCurrent) balance else _state.value.currentBalance,
                    creditCards = if (defineCurrent) balance?.creditCards ?: emptyList() else _state.value.creditCards,
                    lastUpdateTime = if (balance?.wasFetchedFromNetwork == true) Instant.now() else _state.value.lastUpdateTime
                )

                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 300) {
                    delay(300 - elapsed)
                }
                
                _state.value = _state.value.copy(loading = false, isInitialized = true)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false, 
                    error = e.message,
                    isInitialized = true
                )
            }
        }
    }

    private fun loadInvoice(creditCardId: Long, yearMonth: YearMonth) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)
                val invoice = getInvoiceUseCase(creditCardId, yearMonth)
                _state.value = _state.value.copy(selectedInvoice = invoice, loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message)
            }
        }
    }

    private fun selectTransactionById(transactionId: Long) {
        val transaction = _state.value.balance?.transactions?.find { it.id == transactionId }
        val creditCard = _state.value.creditCards.find { card ->
            card.invoices.any { invoice -> 
                invoice.transactions.any { it.id == transactionId }
            }
        }
        _state.value = _state.value.copy(
            selectedTransaction = transaction,
            selectedCreditCard = creditCard
        )
    }

    private fun saveTransaction(transaction: TransactionDTO) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)
                saveTransactionWithCacheUseCase(transaction)
                handleIntent(BalanceIntent.LoadBalance(_state.value.selectedYearMonth, defineCurrent = true))
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message)
            }
        }
    }

    private fun saveCreditCard(creditCard: CreditCardDTO) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)
                saveCreditCardWithCacheUseCase(creditCard)
                handleIntent(BalanceIntent.LoadBalance(_state.value.selectedYearMonth, defineCurrent = true))
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message)
            }
        }
    }

    private fun deleteTransaction(id: Long, all: Boolean) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)
                deleteTransactionWithCacheUseCase(id, all)
                handleIntent(BalanceIntent.LoadBalance(_state.value.selectedYearMonth, defineCurrent = true))
                _deleteTransactionSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message)
            }
        }
    }

    private fun loadInstallments(clickedTransactionId: Long, installmentId: String) {
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _state.value = _state.value.copy(loading = true, error = null)

                val installments = loadInstallmentsUseCase(installmentId)
                val filtered = installments
                    .filter { it?.id != clickedTransactionId }

                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed < 500) {
                    delay(500 - elapsed)
                }

                _state.value = _state.value.copy(installments = filtered, loading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(loading = false, error = e.message)
            }
        }
    }

    private fun updateYearMonth(yearMonth: YearMonth) {
        _state.value = _state.value.copy(selectedYearMonth = yearMonth)
    }

    private fun clearInstallments() {
        _state.value = _state.value.copy(installments = emptyList())
    }

    private fun clear() {
        _state.value = _state.value.copy(
            selectedInvoice = null,
            selectedYearMonth = YearMonth.now(),
            selectedCreditCard = null,
            selectedTransaction = null
        )
    }

    // ✅ Métodos de compatibilidade temporária (para telas que ainda não migraram completamente)
    fun selectCreditCard(creditCard: CreditCardDTO) {
        _state.value = _state.value.copy(
            selectedCreditCard = creditCard,
            selectedInvoice = creditCard.invoices.getOrNull(0)
        )
    }

    fun selectTransaction(transaction: TransactionDTO, creditCard: CreditCardDTO?) {
        _state.value = _state.value.copy(
            selectedTransaction = transaction,
            selectedCreditCard = creditCard
        )
    }

}
