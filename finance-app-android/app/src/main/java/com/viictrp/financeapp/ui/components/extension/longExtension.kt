package com.viictrp.financeapp.ui.components.extension

import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toFormattedYearMonth(format: String? = "MMMM, yyyy"): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .let { YearMonth.from(it) }
        .format(DateTimeFormatter.ofPattern(format, Locale.getDefault()))
}

fun Long.toYearMonth(): YearMonth {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .let { YearMonth.from(it) }
}