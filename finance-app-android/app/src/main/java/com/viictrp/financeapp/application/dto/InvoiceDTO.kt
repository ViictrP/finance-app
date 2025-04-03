package com.viictrp.financeapp.application.dto

data class InvoiceDTO(
    val id: Long,
    val creditCardId: Long,
    val transactions: List<TransactionDTO> = emptyList()
)
