package com.viictrp.financeapp.application.service

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.application.service.mapper.mapTransactionDTO
import com.viictrp.financeapp.graphql.FindInvoiceQuery
import com.viictrp.financeapp.graphql.GetInstallmentsQuery
import com.viictrp.financeapp.graphql.SaveCreditCardMutation
import com.viictrp.financeapp.graphql.SaveCreditCardTransactionMutation
import com.viictrp.financeapp.graphql.type.NewCreditCardDTO
import com.viictrp.financeapp.graphql.type.NewTransactionDTO
import kotlinx.coroutines.CancellationException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

class CreditCardAPIService(private val apolloClient: ApolloClient) {

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth): InvoiceDTO? {
        return try {
            val response: ApolloResponse<FindInvoiceQuery.Data> =
                apolloClient
                    .query(FindInvoiceQuery(creditCardId.toInt(), yearMonth))
                    .execute()

            response.data?.findInvoice?.let { data ->
                InvoiceDTO(
                    id = data.id.toLong(),
                    creditCardId = data.creditCardId.toLong(),
                    transactions = mapTransactionDTO<FindInvoiceQuery.Transaction>(data.transactions) { transaction ->
                        TransactionDTO(
                            id = transaction.id?.toLong(),
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
                    isClosed = data.isClosed == true,
                    yearMonth = YearMonth.of(data.yearMonth?.year ?: 2024, data.yearMonth?.month?.value ?: 1)
                )
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error fetching balance: ${e.message}")
            if (e is CancellationException) throw e
            null
        }
    }

    suspend fun saveCreditCardTransaction(newTransaction: NewTransactionDTO): TransactionDTO? {
        return try {
            val response: ApolloResponse<SaveCreditCardTransactionMutation.Data> =
                apolloClient
                    .mutation(SaveCreditCardTransactionMutation(newTransaction))
                    .execute()

            response.data?.saveCreditCardTransaction?.let { data ->
                TransactionDTO(
                    id = data.id?.toLong(),
                    description = data.description,
                    amount = data.amount,
                    type = TransactionType.valueOf(data.type.toString()),
                    date = LocalDateTime.parse(data.date),
                    isInstallment = data.isInstallment,
                    installmentAmount = data.installmentAmount ?: 1,
                    installmentId = data.installmentId,
                    installmentNumber = data.installmentNumber ?: 0,
                    creditCardId = data.creditCardId?.toLong(),
                    category = data.category.toString()
                )
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error saving new transaction: ${e.message}")
            if (e is CancellationException) throw e
            null
        }
    }

    suspend fun saveCreditCard(newCreditCard: NewCreditCardDTO): CreditCardDTO? {
        return try {
            val response: ApolloResponse<SaveCreditCardMutation.Data> =
                apolloClient
                    .mutation(SaveCreditCardMutation(newCreditCard))
                    .execute()

            response.data?.saveCreditCard?.let { data ->
                CreditCardDTO(
                    id = data.id.toLong(),
                    description = data.description,
                    title = data.title,
                    number = data.number.toString(),
                    invoiceClosingDay = data.invoiceClosingDay,
                    totalInvoiceAmount = data.totalInvoiceAmount ?: BigDecimal.ZERO,
                    color = data.color.toString()
                )
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error saving new credit card: ${e.message}")
            if (e is CancellationException) throw e
            null
        }
    }

    suspend fun loadInstallments(installmentId: String): List<TransactionDTO?> {
        return try {
            val response: ApolloResponse<GetInstallmentsQuery.Data> =
                apolloClient
                    .query(GetInstallmentsQuery(installmentId))
                    .execute()

            response.data?.getInstallments.let { data ->
                data!!.map { transaction ->
                    if (transaction != null) {
                        TransactionDTO(
                            id = transaction.id!!.toLong(),
                            description = transaction.description,
                            amount = transaction.amount,
                            type = TransactionType.valueOf(transaction.type.toString()),
                            date = LocalDateTime.parse(transaction.date),
                            isInstallment = transaction.isInstallment,
                            installmentAmount = transaction.installmentAmount,
                            installmentId = transaction.installmentId,
                            installmentNumber = transaction.installmentNumber,
                            creditCardId = transaction.creditCardId?.toLong(),
                            category = transaction.category.toString()
                        )
                    } else null
                }
            }
        } catch (e: Exception) {
            Log.d("ApiService", "Error saving new credit card: ${e.message}")
            if (e is CancellationException) throw e
            emptyList()
        }
    }
}