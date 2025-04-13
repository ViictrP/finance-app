package com.viictrp.financeapp.ui.components.extension

import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

fun YearMonth.toLong(): Long {
    return this.atDay(LocalDate.now().dayOfMonth)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun YearMonth.toFormattedYearMonth(format: String? = "MMMM, yyyy"): String {
    return this.toLong()
        .toFormattedYearMonth(format)
}