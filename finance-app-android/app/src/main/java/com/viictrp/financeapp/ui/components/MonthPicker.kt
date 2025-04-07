package com.viictrp.financeapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPicker(initial: YearMonth?, onMonthChanged: (YearMonth) -> Unit) {
    var selectedDate by remember {
        mutableStateOf(initial ?: YearMonth.now())
    }
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        datePickerState.selectedDateMillis?.let {
                            val yearMonth = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())  // Convert to ZonedDateTime
                                .toLocalDate()                   // Extract LocalDate
                                .let { YearMonth.from(it) }
                            selectedDate = yearMonth
                            onMonthChanged(yearMonth)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("Pronto", color = MaterialTheme.colorScheme.secondary)
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        "Cancelar",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            )
        }
    }

    OutlinedTextField(
        value = selectedDate?.format(
            DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.getDefault())
        ) ?: "Selecione uma data",
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { openDialog.value = true }) {
                Icon(
                    Icons.Outlined.DateRange,
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "Select Month"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            cursorColor = Color.Transparent,
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium)
    )
}

@Preview(showBackground = true)
@Composable
fun MonthPickerPreview() {
    FinanceAppTheme {
        MonthPicker(initial = YearMonth.now()) { milis ->
            Log.d("MonthPicker", "Month changed: $milis")
        }
    }
}