package com.viictrp.financeapp.application.dto

import com.viictrp.financeapp.application.enums.TransactionType
import java.math.BigDecimal
import java.time.OffsetDateTime

data class TransactionDTO(
    val id: Long,
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: OffsetDateTime,
    val isInstallment: Boolean,
    val installmentAmount: BigDecimal,
    val installmentId: String?,
    val installmentNumber: Int,
    val creditCardId: Long?,
)