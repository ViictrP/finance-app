package com.viictrp.financeapp.domain.repository

import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.InvoiceDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import java.time.YearMonth

interface BalanceRepository {
    suspend fun getBalance(yearMonth: YearMonth): BalanceDTO?
    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth): InvoiceDTO?
    suspend fun saveTransaction(transaction: TransactionDTO): TransactionDTO?
    suspend fun saveCreditCardTransaction(transaction: TransactionDTO): TransactionDTO?
    suspend fun saveCreditCard(creditCard: CreditCardDTO): CreditCardDTO?
    suspend fun loadInstallments(installmentId: String): List<TransactionDTO?>
    suspend fun deleteTransaction(id: Long, all: Boolean)
}
