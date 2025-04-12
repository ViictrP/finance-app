package com.viictrp.financeapp.application.service.mapper

import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.graphql.GetBalanceQuery
import java.math.BigDecimal

internal fun mapCreditCardDTO(creditCards: List<GetBalanceQuery.CreditCard?>): List<CreditCardDTO> =
    creditCards.filterNotNull().map { creditCard ->
        CreditCardDTO(
            id = creditCard.id.toLong(),
            title = creditCard.title,
            description = creditCard.description,
            invoiceClosingDay = creditCard.invoiceClosingDay,
            color = creditCard.color.toString(),
            invoices = mapInvoiceDTO(creditCard.invoices ?: emptyList()),
            number = creditCard.number.toString(),
            totalInvoiceAmount = creditCard.totalInvoiceAmount ?: BigDecimal.ZERO
        )
    }