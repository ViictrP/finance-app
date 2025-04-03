package com.viictrp.financeapp.application.dto

import java.math.BigDecimal

data class CreditCardDTO(
    val id: Long,
    val title: String,
    val description: String,
    val color: String,
    val invoiceClosingDay: Int,
    val totalInvoiceAmount: BigDecimal,
    val invoices: List<InvoiceDTO> = emptyList()
)
