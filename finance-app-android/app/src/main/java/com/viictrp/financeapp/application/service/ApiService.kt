package com.viictrp.financeapp.application.service

import android.util.Log
import com.apollographql.apollo3.api.ApolloResponse
import com.viictrp.financeapp.application.client.ApolloClientProvider
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.graphql.GetBalanceQuery
import kotlinx.coroutines.CancellationException
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.YearMonth

class ApiService {

    val apolloClient = ApolloClientProvider.apolloClient

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
                    transactions = mapTransactionDTO(data.getBalance?.transactions ?: emptyList()),
                    creditCards = mapCreditCardDTO(data.getBalance?.creditCards ?: emptyList()),
                )
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error fetching balance: ${e.message}")
            if (e is CancellationException) throw e
            null
        }
    }

    private fun mapCreditCardDTO(creditCards: List<GetBalanceQuery.CreditCard?>): List<CreditCardDTO> =
        creditCards.filterNotNull().map { creditCard ->
            CreditCardDTO(
                id = creditCard.id.toLong(),
                title = creditCard.title,
                description = creditCard.description,
                invoiceClosingDay = creditCard.invoiceClosingDay,
                color = creditCard.color.toString(),
                invoices = mapInvoiceDTO(creditCard.invoices ?: emptyList()),
                totalInvoiceAmount = creditCard.totalInvoiceAmount ?: BigDecimal.ZERO
            )
        }

    private fun mapInvoiceDTO(invoices: List<GetBalanceQuery.Invoice?>): List<InvoiceDTO> =
        invoices
            .filterNotNull()
            .map { invoice ->
                InvoiceDTO(
                    id = invoice.id.toLong(),
                    creditCardId = invoice.creditCardId.toLong(),
                    transactions = mapTransactionDTO(invoice.transactions)
                )
            }

    private fun mapTransactionDTO(transactions: List<GetBalanceQuery.Transaction?>): List<TransactionDTO> =
        transactions
            .filterNotNull()
            .map { transaction ->
                TransactionDTO(
                    id = transaction.id.toLong(),
                    description = transaction.description,
                    amount = transaction.amount,
                    type = TransactionType.valueOf(transaction.type.toString()),
                    date = OffsetDateTime.parse(transaction.date),
                    isInstallment = transaction.isInstallment,
                    installmentAmount = transaction.installmentAmount ?: BigDecimal.ZERO,
                    installmentId = transaction.installmentId,
                    installmentNumber = transaction.installmentNumber ?: 0,
                    creditCardId = transaction.creditCardId?.toLong()
                )
            }

    @JvmName("mapTransactionsFromType1")
    private fun mapTransactionDTO(transactions: List<GetBalanceQuery.Transaction1?>): List<TransactionDTO> =
        transactions
            .filterNotNull()
            .map { transaction ->
                TransactionDTO(
                    id = transaction.id.toLong(),
                    description = transaction.description,
                    amount = transaction.amount,
                    type = TransactionType.valueOf(transaction.type.toString()),
                    date = OffsetDateTime.parse(transaction.date),
                    isInstallment = transaction.isInstallment,
                    installmentAmount = transaction.installmentAmount ?: BigDecimal.ZERO,
                    installmentId = transaction.installmentId,
                    installmentNumber = transaction.installmentNumber ?: 0,
                    creditCardId = transaction.creditCardId?.toLong()
                )
            }
}