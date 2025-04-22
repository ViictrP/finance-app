package com.viictrp.financeapp.ui.components.custom.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.custom.form.controller.FormController
import com.viictrp.financeapp.ui.components.extension.toLocalDate
import com.viictrp.financeapp.ui.components.extension.toLocalDateTime
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.Accent
import com.viictrp.financeapp.ui.theme.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FDatePickerField(
    form: FormController<*>,
    fieldName: String,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
) {
    val state = form.get(fieldName)
    var showDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = form.getFocusRequester(fieldName)
    val imeAction = form.imeActionFor(fieldName)
    val onNext = form.nextFieldAfter(fieldName)
    val interactionSource = remember { MutableInteractionSource() }
    val value = if (state.text.isNotEmpty()) {
        runCatching {
            state.text.toLong()
                .toLocalDateTime()
                .format(formatter)
        }.getOrElse { "" }
    } else ""

    val selectedDate = remember(state.text) {
        runCatching {
            LocalDate.parse(state.text, formatter)
        }.getOrNull()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )

    val datePickerColors = DatePickerDefaults.colors(
        selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
        todayDateBorderColor = MaterialTheme.colorScheme.secondary,
        todayContentColor = MaterialTheme.colorScheme.secondary,
        weekdayContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
        currentYearContentColor = MaterialTheme.colorScheme.secondary,
        selectedYearContainerColor = MaterialTheme.colorScheme.tertiary,
        selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
        dayContentColor = MaterialTheme.colorScheme.secondary,
        yearContentColor = MaterialTheme.colorScheme.secondary
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release && enabled) {
                showDialog = true
            }
        }
    }

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    "$label ${if (state.required) " *" else ""}",
                    style = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { if (it.isFocused && enabled) showDialog = true }
                .clickable(enabled = enabled) { showDialog = true },
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        painter = CustomIcons.Outline.Calendar,
                        contentDescription = "Abrir calendário",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            isError = state.error != null,
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() ?: focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) },
                onDone = { focusManager.clearFocus() }
            ),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(.8f),
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(.6f),
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorContainerColor = MaterialTheme.colorScheme.primary
            )
        )

        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    IconButton(onClick = {
                        val now = LocalTime.now()
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selected = millis.toLocalDate()
                            form.update(
                                fieldName, "${
                                    LocalDateTime.of(selected, now)
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli()
                                }"
                            )
                            showDialog = false
                        }
                    }) {
                        Icon(
                            painter = CustomIcons.Filled.Save,
                            contentDescription = "Save",
                            tint = Accent
                        )
                    }
                },
                dismissButton = {
                    IconButton(onClick = { showDialog = false }) {
                        Icon(
                            painter = CustomIcons.Filled.Close,
                            contentDescription = "Save",
                            tint = Primary.copy(alpha = .5f)
                        )
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = datePickerColors
                )
            }
        }
    }
}
