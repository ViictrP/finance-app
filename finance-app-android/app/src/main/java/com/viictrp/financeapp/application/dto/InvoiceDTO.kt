package com.viictrp.financeapp.application.dto

import java.time.YearMonth

data class InvoiceDTO(
    val id: Long,
    val creditCardId: Long,
    val transactions: List<TransactionDTO> = emptyList(),
    val isClosed: Boolean,
    val yearMonth: YearMonth
)
