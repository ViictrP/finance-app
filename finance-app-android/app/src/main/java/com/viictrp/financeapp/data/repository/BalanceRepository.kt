package com.viictrp.financeapp.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.viictrp.financeapp.data.common.adapter.LocalDateTimeAdapter
import com.viictrp.financeapp.data.local.dao.BalanceDAO
import com.viictrp.financeapp.data.local.entity.BalanceEntity
import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.service.ApiService
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: BalanceDAO
) {
    private val ttl = 5 * 60 * 1000
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    suspend fun getBalance(yearMonth: YearMonth): BalanceDTO? {
        val key = yearMonth.toString()
        val cached = dao.getBalance(key)
        val now = System.currentTimeMillis()

        if (cached != null && now - cached.lastUpdated < ttl) {
            val balance = deserialize(cached.content)
            delay(100)
            return balance.copy(wasFetchedFromNetwork = false)
        }

        val fresh = apiService.getBalance(yearMonth)
        return fresh?.let {
            dao.saveBalance(
                BalanceEntity(
                    yearMonth = key,
                    content = serialize(it),
                    lastUpdated = now
                )
            )
            return it.copy(wasFetchedFromNetwork = true)
        }
    }

    suspend fun clearCache() {
        dao.clearBalance()
    }

    private fun serialize(balance: BalanceDTO): String {
        return gson.toJson(balance)
    }

    private fun deserialize(json: String): BalanceDTO {
        return gson.fromJson(json, BalanceDTO::class.java)
    }
}