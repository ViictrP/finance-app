package com.viictrp.financeapp.application.service

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.application.service.mapper.mapCreditCardDTO
import com.viictrp.financeapp.application.service.mapper.mapMonthClosureDTO
import com.viictrp.financeapp.application.service.mapper.mapRecurringExpenseDTO
import com.viictrp.financeapp.application.service.mapper.mapTransactionDTO
import com.viictrp.financeapp.graphql.GetBalanceQuery
import kotlinx.coroutines.CancellationException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

class UserAPIService(private val apolloClient: ApolloClient) {

    suspend fun getBalance(yearMonth: YearMonth): BalanceDTO? {
        return try {
            val response: ApolloResponse<GetBalanceQuery.Data> =
                apolloClient
                    .query(GetBalanceQuery(yearMonth))
                    .execute()

            response.data?.let { data ->
                BalanceDTO(
                    salary = data.getBalance?.salary ?: BigDecimal.ZERO,
                    expenses = data.getBalance?.expenses ?: BigDecimal.ZERO,
                    taxValue = data.getBalance?.taxValue ?: BigDecimal.ZERO,
                    available = data.getBalance?.available ?: BigDecimal.ZERO,
                    exchangeTaxValue = data.getBalance?.exchangeTaxValue ?: BigDecimal.ZERO,
                    nonConvertedSalary = data.getBalance?.nonConvertedSalary ?: BigDecimal.ZERO,
                    transactions = mapTransactionDTO(data.getBalance?.transactions ?: emptyList()) { transaction ->
                        TransactionDTO(
                            id = transaction.id.toLong(),
                            description = transaction.description,
                            amount = transaction.amount,
                            type = TransactionType.valueOf(transaction.type.toString()),
                            date = LocalDateTime.parse(transaction.date),
                            isInstallment = transaction.isInstallment,
                            installmentAmount = transaction.installmentAmount ?: 1,
                            installmentId = transaction.installmentId,
                            installmentNumber = transaction.installmentNumber ?: 0,
                            creditCardId = transaction.creditCardId?.toLong(),
                            category = transaction.category.toString()
                        )
                    },
                    creditCards = mapCreditCardDTO(data.getBalance?.creditCards ?: emptyList()),
                    recurringExpenses = mapRecurringExpenseDTO(
                        data.getBalance?.recurringExpenses ?: emptyList()
                    ),
                    monthClosures = mapMonthClosureDTO(
                        data.getBalance?.monthClosures ?: emptyList()
                    ),
                )
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error fetching balance: ${e.message}")
            if (e is CancellationException) throw e
            null
        }
    }
}