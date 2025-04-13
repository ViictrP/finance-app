package com.viictrp.financeapp.application.service.mapper

import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.graphql.GetBalanceQuery
import java.math.BigDecimal
import java.time.OffsetDateTime
import kotlin.toString

internal fun mapRecurringExpenseDTO(recurringExpenses: List<GetBalanceQuery.RecurringExpense?>): List<TransactionDTO> {
    return recurringExpenses
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
                creditCardId = transaction.creditCardId?.toLong(),
                category = transaction.category.toString()
            )
        }
}