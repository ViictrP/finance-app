package com.viictrp.financeapp.data.remote.dto

import java.math.BigDecimal

data class MonthClosureDTO(
    val month: String,
    val year: Int,
    val total: BigDecimal,
    val available: BigDecimal,
    val expenses: BigDecimal,
    val index: Int,
    val finalUsdToBRL: BigDecimal
)
