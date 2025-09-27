package com.viictrp.financeapp.ui.screens.secure.creditcard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.ui.components.CustomIcons
import com.viictrp.financeapp.ui.components.FSelectField
import com.viictrp.financeapp.ui.components.FSelectItem
import com.viictrp.financeapp.ui.components.FTextField
import com.viictrp.financeapp.ui.components.LoadingDialog
import com.viictrp.financeapp.ui.components.formutils.controller.Field
import com.viictrp.financeapp.ui.components.formutils.controller.StateValidator
import com.viictrp.financeapp.ui.components.formutils.controller.StateValidatorType
import com.viictrp.financeapp.ui.components.formutils.controller.rememberFormController
import com.viictrp.financeapp.ui.theme.Accent
import com.viictrp.financeapp.ui.theme.Orange
import com.viictrp.financeapp.ui.theme.Purple
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreditCardFormScreen(padding: PaddingValues) {
    val viewModel = rememberBalanceViewModel()

    val spacing = 48.dp

    // ✅ FULL MVI - Apenas state
    val state by viewModel.state.collectAsState()
    val coroutine = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val colorOptions by remember {
        mutableStateOf(
            listOf(
            object : FSelectItem {
                override fun getLabel(): String {
                    return "Azul"
                }

                override fun getValue(): String {
                    return "azul"
                }

                override fun getIcon(): @Composable (() -> Unit)? {
                    return {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Accent,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
                        )
                    }
                }
            },
            object : FSelectItem {
                override fun getLabel(): String {
                    return "Laranja"
                }

                override fun getValue(): String {
                    return "orange"
                }

                override fun getIcon(): @Composable (() -> Unit)? {
                    return {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Orange,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
                        )
                    }
                }
            },
            object : FSelectItem {
                override fun getLabel(): String {
                    return "Preto"
                }

                override fun getValue(): String {
                    return "black"
                }

                override fun getIcon(): @Composable (() -> Unit)? {
                    return {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Secondary,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
                        )
                    }
                }
            },
            object : FSelectItem {
                override fun getLabel(): String {
                    return "Roxo"
                }

                override fun getValue(): String {
                    return "purple"
                }

                override fun getIcon(): @Composable (() -> Unit)? {
                    return {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Purple,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(.1f)),
                        )
                    }
                }
            }
        ))
    }

    val form = rememberFormController<CreditCardDTO>(
        listOf(
            Field(
                "title",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(
                "description",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(
                "number", required = true, validators = listOf(
                    StateValidatorType.REQUIRED.validator,
                    StateValidator(
                        validator = {
                            val number = it.toIntOrNull()
                            number != null
                        },
                        errorMessage = "Informe um número"
                    )
                )
            ),
            Field(
                "invoiceClosingDay", required = true, validators = listOf(
                    StateValidatorType.REQUIRED.validator,
                    StateValidator(
                        validator = {
                            val number = it.toIntOrNull()
                            number != null && number in 1..31
                        },
                        errorMessage = "Informe um número entre 1 e 31"
                    )
                )
            ),
            Field("color")
        )
    ) { fields ->
        CreditCardDTO(
            title = fields["title"]!!.text,
            description = fields["description"]!!.text,
            color = fields["color"]!!.text,
            number = fields["number"]!!.text,
            invoiceClosingDay = fields["invoiceClosingDay"]!!.text.toInt(),
            totalInvoiceAmount = BigDecimal.ZERO,
            invoices = emptyList()
        )
    }

    val isEnabled = form.isValid

    if (showDialog) {
        LoadingDialog(loading = state.loading)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEnabled) {
                        coroutine.launch {
                            showDialog = true
                            // ✅ MVI - Usando handleIntent
                            viewModel.handleIntent(BalanceIntent.SaveCreditCard(form.value))
                            delay(500)
                            form.clear()
                            showDialog = false
                        }
                    }
                },
                containerColor = if (isEnabled) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                contentColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .alpha(if (isEnabled) 1f else 0.6f)
            ) {
                Icon(
                    painter = CustomIcons.Filled.Save,
                    contentDescription = "Salvar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Adicionar um Cartão de Crédito",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 24.sp)
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        " Para adicionar um cartão de crédito, preencha todas as informações obrigatórias marcadas com * (asterísco). ",
                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    FTextField(
                        form = form,
                        fieldName = "title",
                        label = "Título do cartão",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Description
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    FTextField(
                        form = form,
                        fieldName = "description",
                        label = "Descrição do cartão",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Description
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    FTextField(
                        form = form,
                        fieldName = "number",
                        label = "Número do cartão",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Number,
                        keyboardType = KeyboardType.Number,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    FTextField(
                        form = form,
                        fieldName = "invoiceClosingDay",
                        label = "Data de fechamento",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Number,
                        leadingIcon = CustomIcons.Outline.Calendar
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    FSelectField(
                        form = form,
                        fieldName = "color",
                        label = "Cor do cartão",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Color,
                        options = colorOptions
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }
        }
    }
}