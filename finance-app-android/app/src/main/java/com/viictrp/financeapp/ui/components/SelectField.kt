package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.formutils.controller.Field
import com.viictrp.financeapp.ui.components.formutils.controller.FormController
import com.viictrp.financeapp.ui.components.formutils.controller.StateValidatorType
import com.viictrp.financeapp.ui.components.formutils.controller.rememberFormController
import com.viictrp.financeapp.ui.theme.Blue
import com.viictrp.financeapp.ui.theme.Orange

interface FSelectItem {
    fun getLabel(): String
    fun getValue(): String
    fun getIcon(): (@Composable () -> Unit)?
    fun isSelected(): Boolean = false
}

@Composable
fun FSelectField(
    form: FormController<*>,
    fieldName: String,
    options: List<FSelectItem>,
    modifier: Modifier = Modifier,
    label: String = "",
    leadingIcon: Painter? = null,
    enabled: Boolean = true
) {
    val state = form.get(fieldName)
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    val focusRequester = form.getFocusRequester(fieldName)
    val imeAction = form.imeActionFor(fieldName)
    val onNext = form.nextFieldAfter(fieldName)

    val optionToDisplay = options.find { option ->
        option.getValue() == state.text
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release && enabled) {
                expanded = !expanded
            }
        }
    }

    LaunchedEffect(Unit) {
        val preSelectedOption = options.find { option -> option.isSelected() }
        preSelectedOption?.let {
            form.update(fieldName, it.getValue())
        }
    }

    Column(modifier = modifier) {
        Box {
            TextField(
                value = optionToDisplay?.getLabel() ?: state.text,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clickable(enabled = enabled) { expanded = true }
                    .onFocusChanged { if (it.isFocused) expanded = true },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNext?.invoke() ?: focusManager.moveFocus(FocusDirection.Down)
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                interactionSource = interactionSource,
                label = {
                    Text(
                        "$label ${if (state.required) " *" else ""}",
                        style = LocalTextStyle.current.copy(fontSize = 16.sp)
                    )
                },
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
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            painter = CustomIcons.Outline.CaretDown,
                            contentDescription = "Abrir seleção",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                isError = state.error != null,
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(.8f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(.6f),
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(.92f)
                    .heightIn(max = 300.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f))
            ) {
                options.forEach { option ->
                    val label = option.getLabel()
                    val value = option.getValue()
                    val icon = option.getIcon()
                    DropdownMenuItem(
                        text = { Text(label) },
                        leadingIcon = {
                            if (icon != null) {
                                icon()
                            }
                        },
                        onClick = {
                            form.update(fieldName, value)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FSelectFieldPreview() {
    data class SimpleSelectItem(
        private val label: String,
        private val value: String = label,
        private val icon: (@Composable () -> Unit)? = null
    ) : FSelectItem {
        override fun getLabel(): String = label
        override fun getValue(): String = value
        override fun getIcon(): (@Composable () -> Unit)? = icon
    }

    val options = listOf<FSelectItem>(
        SimpleSelectItem("Vermelho", icon = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Orange,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
            )
        }),
        SimpleSelectItem("Azul", icon = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Blue,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
            )
        }),
        SimpleSelectItem("Outro Azul", icon = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        Blue,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
            )
        })
    )

    val form = rememberFormController(
        fields = listOf(
            Field("color", required = true, validators = listOf(StateValidatorType.REQUIRED.validator))
        ),
        toDto = { it } // Preview não precisa de DTO real
    )

    FSelectField(
        form = form,
        fieldName = "color",
        options = options,
        label = "Cor do cartão",
        leadingIcon = CustomIcons.Outline.Color
    )
}
