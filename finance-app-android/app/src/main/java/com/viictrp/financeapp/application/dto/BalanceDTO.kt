package com.viictrp.financeapp.application.dto

import java.math.BigDecimal

data class BalanceDTO(
    val transactions: List<TransactionDTO> = emptyList(),
    val recurringExpenses: List<TransactionDTO> = emptyList(),
    val creditCards: List<CreditCardDTO> = emptyList(),
    val salary: BigDecimal = BigDecimal.ZERO,
    val expenses: BigDecimal = BigDecimal.ZERO,
    val available: BigDecimal = BigDecimal.ZERO,
    val taxValue: BigDecimal = BigDecimal.ZERO,
    val exchangeTaxValue: BigDecimal = BigDecimal.ZERO,
    val nonConvertedSalary: BigDecimal = BigDecimal.ZERO,
    val creditCardExpenses: Map<Long, BigDecimal> = emptyMap()
)