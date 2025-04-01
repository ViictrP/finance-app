package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPicker() {
    val formatter = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
    var selectedDate by remember {
        mutableStateOf(formatter.format(Calendar.getInstance().time))
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
                        selectedDate = formatter.format(datePickerState.selectedDateMillis)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("Pronto")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
            )
        }
    }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
        trailingIcon = {
            IconButton(onClick = { openDialog.value = true }) {
                Icon(Icons.Outlined.DateRange, contentDescription = "Select Month")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Transparent,
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
    )
}