package com.viictrp.financeapp.application.dto

import androidx.compose.runtime.Immutable
import com.viictrp.financeapp.application.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

@Immutable
data class TransactionDTO(
    val id: Long? = null,
    val description: String,
    val amount: BigDecimal,
    val category: String,
    val type: TransactionType,
    val date: LocalDateTime,
    val isInstallment: Boolean? = false,
    val installmentAmount: Int? = 1,
    val installmentId: String? = null,
    val installmentNumber: Int? = 1,
    val creditCardId: Long?,
)