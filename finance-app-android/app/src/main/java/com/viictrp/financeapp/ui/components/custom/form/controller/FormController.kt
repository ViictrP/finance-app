package com.viictrp.financeapp.ui.components.custom.form.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction

class FormController(
    fields: Map<String, InputFieldState>,
    private val focusRequesters: Map<String, FocusRequester>
) {
    private val _fields: SnapshotStateMap<String, InputFieldState> =
        mutableStateMapOf<String, InputFieldState>().apply { putAll(fields) }
    val fields: Map<String, InputFieldState> get() = _fields
    val value: Map<String, String> get() = _fields.mapValues { it.value.text }

    val isValid: Boolean
        get() = _fields.values.all { it.isValid }

    fun get(name: String): InputFieldState = _fields[name] ?: error("Campo '$name' não encontrado no form")

    fun update(name: String, text: String) {
        val prev = _fields[name] ?: error("Campo '$name' não existe")
        _fields[name] = prev.update(text)
    }

    fun getFocusRequester(fieldName: String): FocusRequester =
        focusRequesters[fieldName] ?: FocusRequester()

    fun nextFieldAfter(current: String): (() -> Unit)? {
        val keys = focusRequesters.keys.toList()
        val index = keys.indexOf(current)
        if (index != -1 && index + 1 < keys.size) {
            val nextKey = keys[index + 1]
            return { focusRequesters[nextKey]?.requestFocus() }
        }
        return null
    }

    fun imeActionFor(fieldName: String): ImeAction =
        if (focusRequesters.keys.lastOrNull() == fieldName) ImeAction.Done else ImeAction.Next
}

data class Field(
    val name: String = "",
    val required: Boolean = false,
    val validators: List<StateValidator> = emptyList()
)

@Composable
fun rememberFormController(fields: List<Field>): FormController {
    val focusRequesters = remember(fields) {
        fields.associate { it.name to FocusRequester() }
    }

    return rememberSaveable(
        saver = mapSaver(
            save = { controller ->
                controller.fields.mapValues { entry ->
                    listOf(
                        entry.value.text,
                        entry.value.touched,
                        entry.value.dirty,
                        entry.value.error,
                        entry.value.required
                    )
                }
            },
            restore = {
                val restored = it.mapValues { entry ->
                    val list = entry.value as List<*>
                    InputFieldState(
                        text = list[0] as String,
                        touched = list[1] as Boolean,
                        dirty = list[2] as Boolean,
                        error = list[3] as String?,
                        required = list[4] as Boolean,
                        validators = mutableListOf()
                    )
                }.toMutableMap()

                fields.forEach { field ->
                    restored[field.name] = restored[field.name]?.copy(validators = field.validators)
                        ?: InputFieldState(required = field.required, validators = field.validators)
                }

                FormController(restored, focusRequesters)
            }
        )
    ) {
        val initial = fields.associate { field ->
            field.name to InputFieldState(required = field.required, validators = field.validators)
        }
        FormController(initial, focusRequesters)
    }
}


