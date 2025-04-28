package com.viictrp.financeapp.ui.components.formutils.controller

import com.viictrp.financeapp.ui.components.extension.toLocalDateTime
import java.math.BigDecimal
import java.time.LocalDateTime

enum class StateValidatorType(val validator: StateValidator) {
    REQUIRED(
        StateValidator(
            validator = { it.isNotBlank() },
            errorMessage = "Campo obrigatório"
        )
    ),

    EMAIL(
        StateValidator(
            validator = { it.contains("@") && it.contains(".") },
            errorMessage = "Email inválido"
        )
    )
}

data class StateValidator(
    val validator: (String) -> Boolean,
    val errorMessage: String
)

data class InputFieldState(
    val text: String = "",
    val touched: Boolean = false,
    val dirty: Boolean = false,
    val error: String? = null,
    val required: Boolean = false,
    val validators: List<StateValidator> = emptyList()
) {
    val isValid: Boolean
        get() = validators.all { it.validator(text) }

    fun update(newText: String): InputFieldState {
        val firstFailed = validators.firstOrNull { !it.validator(newText) }
        return copy(
            text = newText,
            touched = true,
            dirty = true,
            error = firstFailed?.errorMessage
        )
    }
}

val InputFieldState.textValue: String?
    get() = text.takeIf { it.isNotBlank() }

val InputFieldState.longValue: Long?
    get() = text.takeIf { it.isNotBlank() }?.toLongOrNull()

val InputFieldState.intValue: Int?
    get() = text.takeIf { it.isNotBlank() }?.toIntOrNull()

val InputFieldState.decimalValue: BigDecimal?
    get() = text.takeIf { it.isNotBlank() }?.replace(",", ".")?.toBigDecimalOrNull()

val InputFieldState.localDateTimeValue: LocalDateTime?
    get() = text.takeIf { it.isNotBlank() }?.let {
        runCatching { it.toLong().toLocalDateTime() }.getOrNull()
    }