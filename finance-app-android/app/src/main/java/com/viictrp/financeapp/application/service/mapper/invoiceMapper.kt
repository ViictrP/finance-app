package com.viictrp.financeapp.application.service.mapper

import com.viictrp.financeapp.application.dto.InvoiceDTO
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.graphql.GetBalanceQuery
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

internal fun mapInvoiceDTO(invoices: List<GetBalanceQuery.Invoice?>): List<InvoiceDTO> =
    invoices
        .filterNotNull()
        .map { invoice ->
            InvoiceDTO(
                id = invoice.id.toLong(),
                creditCardId = invoice.creditCardId.toLong(),
                transactions = mapTransactionDTO(invoice.transactions) { transaction ->
                    TransactionDTO(
                        id = transaction.id.toLong(),
                        description = transaction.description,
                        amount = transaction.amount,
                        type = TransactionType.valueOf(transaction.type.toString()),
                        date = LocalDateTime.parse(transaction.date),
                        isInstallment = transaction.isInstallment,
                        installmentAmount = transaction.installmentAmount ?: BigDecimal.ZERO,
                        installmentId = transaction.installmentId,
                        installmentNumber = transaction.installmentNumber ?: 0,
                        creditCardId = transaction.creditCardId?.toLong(),
                        category = transaction.category.toString()
                    )
                },
                isClosed = false,
                yearMonth = YearMonth.now()
            )
        }