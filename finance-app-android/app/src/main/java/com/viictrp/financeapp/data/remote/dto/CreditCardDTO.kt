package com.viictrp.financeapp.data.remote.dto

import java.math.BigDecimal

data class CreditCardDTO(
    val id: Long? = null,
    val title: String,
    val description: String,
    val color: String,
    val number: String,
    val invoiceClosingDay: Int,
    val totalInvoiceAmount: BigDecimal,
    val invoices: List<InvoiceDTO> = emptyList()
)
