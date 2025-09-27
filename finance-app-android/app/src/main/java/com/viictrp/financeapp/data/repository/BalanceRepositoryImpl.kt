package com.viictrp.financeapp.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.viictrp.financeapp.data.common.adapter.LocalDateTimeAdapter
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import com.viictrp.financeapp.data.local.entity.BalanceEntity
import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.InvoiceDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.data.remote.service.ApiService
import com.viictrp.financeapp.domain.repository.BalanceRepository
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: BalanceDAO
) : BalanceRepository {
    private val ttl = 5 * 60 * 1000
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    override suspend fun getBalance(yearMonth: YearMonth): BalanceDTO? {
        val key = yearMonth.toString()
        val cached = dao.getBalance(key)
        val now = System.currentTimeMillis()

        if (cached != null && now - cached.lastUpdated < ttl) {
            val balance = deserialize(cached.content)
            return balance
        }

        val balance = apiService.getBalance(yearMonth)
        return balance?.let {
            val entity = BalanceEntity(
                yearMonth = key,
                content = serialize(balance),
                lastUpdated = now
            )
            dao.saveBalance(entity)
            return it.copy(wasFetchedFromNetwork = true)
        }
    }

    override suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth): InvoiceDTO? {
        return apiService.getInvoice(creditCardId, yearMonth)
    }

    override suspend fun saveTransaction(transaction: TransactionDTO): TransactionDTO? {
        return apiService.saveUserCardTransaction(transaction)
    }

    override suspend fun saveCreditCardTransaction(transaction: TransactionDTO): TransactionDTO? {
        return apiService.saveCreditCardTransaction(transaction)
    }

    override suspend fun saveCreditCard(creditCard: CreditCardDTO): CreditCardDTO? {
        return apiService.saveCreditCard(creditCard)
    }

    override suspend fun loadInstallments(installmentId: String): List<TransactionDTO?> {
        return apiService.loadInstallments(installmentId)
    }

    override suspend fun deleteTransaction(id: Long, all: Boolean) {
        apiService.deleteTransaction(id, all)
    }

    override suspend fun clearCache() {
        dao.clearBalance()
    }

    private fun serialize(balance: BalanceDTO): String {
        return gson.toJson(balance)
    }

    private fun deserialize(content: String): BalanceDTO {
        return gson.fromJson(content, BalanceDTO::class.java)
    }
}
