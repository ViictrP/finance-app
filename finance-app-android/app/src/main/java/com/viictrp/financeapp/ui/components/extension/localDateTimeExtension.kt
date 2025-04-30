package com.viictrp.financeapp.ui.components.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.toFormatted(): String {
    return this
        .format(DateTimeFormatter.ofPattern("dd MMMM, yyyy - HH:mm", Locale.getDefault()))
}