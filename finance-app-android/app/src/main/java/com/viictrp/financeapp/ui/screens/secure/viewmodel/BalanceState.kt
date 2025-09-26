package com.viictrp.financeapp.ui.screens.secure.viewmodel

import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.InvoiceDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import java.time.Instant
import java.time.YearMonth

data class BalanceState(
    val loading: Boolean = false,
    val balance: BalanceDTO? = null,
    val currentBalance: BalanceDTO? = null,
    val selectedInvoice: InvoiceDTO? = null,
    val selectedCreditCard: CreditCardDTO? = null,
    val selectedTransaction: TransactionDTO? = null,
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val creditCards: List<CreditCardDTO> = emptyList(),
    val installments: List<TransactionDTO?> = emptyList(),
    val lastUpdateTime: Instant? = null,
    val isInitialized: Boolean = false,
    val error: String? = null
)

sealed class BalanceIntent {
    data class LoadBalance(val yearMonth: YearMonth, val defineCurrent: Boolean = false) : BalanceIntent()
    object RefreshBalance : BalanceIntent()
    data class UpdateYearMonth(val yearMonth: YearMonth) : BalanceIntent()
    data class LoadInvoice(val creditCardId: Long, val yearMonth: YearMonth) : BalanceIntent()
    data class SelectTransaction(val transactionId: Long) : BalanceIntent()
    data class SaveTransaction(val transaction: TransactionDTO) : BalanceIntent()
    data class SaveCreditCard(val creditCard: CreditCardDTO) : BalanceIntent()
    data class DeleteTransaction(val id: Long, val all: Boolean = false) : BalanceIntent()
    data class LoadInstallments(val clickedTransactionId: Long, val installmentId: String) : BalanceIntent()
    object ClearCache : BalanceIntent()
    object Clear : BalanceIntent()
}
