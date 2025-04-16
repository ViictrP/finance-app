package com.viictrp.financeapp.ui.components.custom.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.viictrp.financeapp.ui.components.custom.form.controller.FormController
import com.viictrp.financeapp.ui.components.icon.CustomIcons

@Composable
fun FTextField(
    form: FormController,
    fieldName: String,
    modifier: Modifier = Modifier,
    label: String = "",
    leadingIcon: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    var showTooltip by remember { mutableStateOf(false) }
    val state = form.get(fieldName)

    TextField(
        value = state.text,
        onValueChange = { form.update(fieldName, it) },
        label = { Text("$label ${if (state.required) " *" else ""}") },
        modifier = modifier,
        isError = state.required && state.error != null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                )
            }
        },
        trailingIcon = {
            Box {
                if (state.text.isNotBlank() && state.error == null) {
                    IconButton(onClick = {
                        form.update(fieldName, "")
                    }) {
                        Icon(
                            painter = CustomIcons.Outline.Close,
                            contentDescription = "Limpar",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else
                    if (state.error != null && state.required) {
                    IconButton(onClick = { showTooltip = !showTooltip }) {
                        Icon(
                            painter = CustomIcons.Filled.Info,
                            contentDescription = "Erro",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (showTooltip) {
                        Popup(
                            alignment = Alignment.TopEnd,
                            offset = IntOffset(x = -100, y = -50),
                            onDismissRequest = { showTooltip = false }
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary.copy(alpha = .1f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = state.error,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}
