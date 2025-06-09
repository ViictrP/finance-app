package com.viictrp.financeapp.ui.components.extension

import java.time.YearMonth
import java.time.ZoneId

fun YearMonth.toLong(): Long {
    return this.atDay(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}