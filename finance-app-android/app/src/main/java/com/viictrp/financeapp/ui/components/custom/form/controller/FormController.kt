package com.viictrp.financeapp.ui.components.custom.form.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable

class FormController(fields: Map<String, InputFieldState>) {
    private val _fields = mutableStateMapOf<String, InputFieldState>().apply { putAll(fields) }
    val fields: Map<String, InputFieldState> get() = _fields

    val isValid: Boolean
        get() = _fields.values.all { it.isValid }

    fun get(name: String): InputFieldState = _fields[name] ?: InputFieldState()

    fun update(name: String, text: String, validator: (String) -> String?) {
        val prev = _fields[name] ?: InputFieldState()
        _fields[name] = prev.update(text, validator)
    }

    fun updateFieldState(name: String, state: InputFieldState) {
        _fields[name] = state
    }

    fun markTouched(name: String) {
        _fields[name]?.let {
            _fields[name] = it.copy(touched = true)
        }
    }

    fun markAllTouched() {
        _fields.keys.forEach { markTouched(it) }
    }
}

@Composable
fun rememberFormController(): FormController {
    return rememberSaveable(saver = mapSaver(
        save = { it.fields.mapValues { entry -> listOf(entry.value.text, entry.value.touched, entry.value.dirty, entry.value.error) } },
        restore = {
            val restored = it.mapValues { entry ->
                val list = entry.value as List<*>
                InputFieldState(
                    text = list[0] as String,
                    touched = list[1] as Boolean,
                    dirty = list[2] as Boolean,
                    error = list[3] as String?
                )
            }
            FormController(restored)
        }
    )) {
        FormController(
            mapOf(
                "title" to InputFieldState(),
                "description" to InputFieldState(),
                "number" to InputFieldState(),
                "closingDate" to InputFieldState(),
                "color" to InputFieldState()
            )
        )
    }
}

