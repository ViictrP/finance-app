package com.viictrp.financeapp.application.service

import com.apollographql.apollo3.api.Optional
import com.viictrp.financeapp.application.client.ApolloClientProvider
import com.viictrp.financeapp.application.dto.BalanceDTO
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.graphql.type.NewCreditCardDTO
import com.viictrp.financeapp.graphql.type.NewTransactionDTO
import com.viictrp.financeapp.graphql.type.TransactionType
import java.time.YearMonth

class ApiService {

    val apolloClient = ApolloClientProvider.apolloClient
    val creditCardApiService = CreditCardAPIService(apolloClient)
    val userApiService = UserAPIService(apolloClient)

    suspend fun getBalance(yearMonth: YearMonth): BalanceDTO? {
        return userApiService.getBalance(yearMonth)
    }

    suspend fun getInvoice(creditCardId: Long, yearMonth: YearMonth): InvoiceDTO? {
        return creditCardApiService.getInvoice(creditCardId, yearMonth)
    }

    suspend fun saveCreditCardTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        val newTransaction = NewTransactionDTO(
            description = newTransactionDTO.description,
            amount = newTransactionDTO.amount,
            type = TransactionType.valueOf(newTransactionDTO.type.toString()),
            date = newTransactionDTO.date.toString(),
            category = newTransactionDTO.category,
            installmentAmount = newTransactionDTO.installmentAmount ?: 1,
            creditCardId = Optional.present(newTransactionDTO.creditCardId?.toInt()),
        )
        return creditCardApiService.saveCreditCardTransaction(newTransaction)
    }

    suspend fun saveUserCardTransaction(newTransactionDTO: TransactionDTO): TransactionDTO? {
        val newTransaction = NewTransactionDTO(
            description = newTransactionDTO.description,
            amount = newTransactionDTO.amount,
            type = TransactionType.valueOf(newTransactionDTO.type.toString()),
            date = newTransactionDTO.date.toString(),
            category = newTransactionDTO.category,
            installmentAmount = newTransactionDTO.installmentAmount ?: 1,
            creditCardId = Optional.present(newTransactionDTO.creditCardId?.toInt()),
        )
        return userApiService.saveTransaction(newTransaction)
    }

    suspend fun saveCreditCard(newCreditCardDTO: CreditCardDTO): CreditCardDTO? {
        val newCreditCard = NewCreditCardDTO(
            description = newCreditCardDTO.description,
            title = newCreditCardDTO.title,
            number = newCreditCardDTO.number,
            invoiceClosingDay = newCreditCardDTO.invoiceClosingDay,
            color = Optional.present(newCreditCardDTO.color),
        )
        return creditCardApiService.saveCreditCard(newCreditCard)
    }
}