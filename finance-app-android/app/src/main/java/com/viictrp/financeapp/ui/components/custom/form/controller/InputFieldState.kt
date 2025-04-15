package com.viictrp.financeapp.ui.components.custom.form.controller

data class InputFieldState(
    val text: String = "",
    val touched: Boolean = false,
    val dirty: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean get() = error == null && text.isNotBlank()

    fun validate(validator: (String) -> String?): InputFieldState {
        return copy(
            touched = true,
            dirty = true,
            error = validator(text)
        )
    }
    fun update(newText: String, validator: (String) -> String?): InputFieldState {
        return copy(
            text = newText,
            touched = true,
            dirty = true,
            error = validator(newText)
        )
    }
}