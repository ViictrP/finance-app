package com.viictrp.financeapp.data.remote.service.mapper

import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
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