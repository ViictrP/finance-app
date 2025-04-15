package com.viictrp.financeapp.ui.components.custom.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.viictrp.financeapp.ui.components.custom.form.controller.InputFieldState
import com.viictrp.financeapp.ui.components.icon.CustomIcons

@Composable
fun FTextField(
    state: InputFieldState,
    onStateChanged: (InputFieldState) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    tooltipMessage: String? = null,
    showError: Boolean = false,
    leadingIcon: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    var showTooltip by remember { mutableStateOf(false) }

    TextField(
        value = state.text,
        onValueChange = { newText ->
            val updated = state.update(newText) { text ->
                if (text.isBlank()) "Campo obrigatório" else null
            }
            onStateChanged(updated)
        },
        isError = state.error != null,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        enabled = enabled,
        modifier = modifier,
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    tint = if (state.error != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.tertiary
                )
            }
        },
        trailingIcon = {
            Box {
                if (state.error != null && tooltipMessage != null && showError) {
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
                                    .padding(8.dp)
                                    .shadow(4.dp)
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
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorTrailingIconColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorTextColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    FTextField(
        modifier = Modifier.fillMaxWidth(),
        state = TODO(),
        onStateChanged = TODO(),
        label = TODO(),
        tooltipMessage = TODO(),
        showError = TODO(),
        leadingIcon = TODO(),
        keyboardType = TODO(),
        enabled = TODO()
    )
}