package com.viictrp.financeapp.ui.screens.main.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.custom.LoadingDialog
import com.viictrp.financeapp.ui.components.custom.form.FDatePickerField
import com.viictrp.financeapp.ui.components.custom.form.FSelectField
import com.viictrp.financeapp.ui.components.custom.form.FSelectItem
import com.viictrp.financeapp.ui.components.custom.form.FTextField
import com.viictrp.financeapp.ui.components.custom.form.controller.Field
import com.viictrp.financeapp.ui.components.custom.form.controller.StateValidator
import com.viictrp.financeapp.ui.components.custom.form.controller.StateValidatorType
import com.viictrp.financeapp.ui.components.custom.form.controller.rememberFormController
import com.viictrp.financeapp.ui.components.extension.toLocalDateTime
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun TransactionFormScreen(balanceModel: BalanceViewModel) {
    val spacing = 48.dp

    val balance = balanceModel.currentBalance.collectAsState()
    val coroutine = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val loading = balanceModel.loading.collectAsState()
    val creditCards = balance.value?.creditCards ?: emptyList()

    val creditCardOptions = creditCards
        .map {
            object : FSelectItem {
                override fun getLabel(): String = it.title
                override fun getValue(): String = it.id.toString()
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.CreditCard,
                        contentDescription = it.title,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }

    val typeOptions by remember {
        mutableStateOf(
            listOf<FSelectItem>(
            object : FSelectItem {
                override fun getLabel(): String = "Normal"
                override fun getValue(): String = "DEFAULT"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.Invoice,
                        contentDescription = "Normal",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            object : FSelectItem {
                override fun getLabel(): String = "Recorrente"
                override fun getValue(): String = "RECURRING"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.Invoice,
                        contentDescription = "Recorrente",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        ))
    }
    val categoryOptions by remember {
        mutableStateOf(
            listOf<FSelectItem>(
            object : FSelectItem {
                override fun getLabel(): String = "Casa"
                override fun getValue(): String = "home"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.House,
                        contentDescription = "Casa",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            object : FSelectItem {
                override fun getLabel(): String = "Restaurante"
                override fun getValue(): String = "food"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.Burger,
                        contentDescription = "Restaurante",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            object : FSelectItem {
                override fun getLabel(): String = "Shop"
                override fun getValue(): String = "shop"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.ShoppingBag,
                        contentDescription = "Shop",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            object : FSelectItem {
                override fun getLabel(): String = "Cartão"
                override fun getValue(): String = "credit_card"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.CreditCard,
                        contentDescription = "Cartão",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            object : FSelectItem {
                override fun getLabel(): String = "Outro"
                override fun getValue(): String = "other"
                override fun getIcon(): @Composable (() -> Unit)? = {
                    Icon(
                        painter = CustomIcons.Outline.Barcode,
                        contentDescription = "Outro",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        ))
    }

    val form = rememberFormController<TransactionDTO>(
        listOf(
            Field(
                name = "type",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(
                name = "category",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(
                name = "description",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(
                name = "amount", required = true, validators = listOf(
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
                name = "installmentAmount",
                initialValue = "1",
                required = false,
                validators = listOf(
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
                name = "date",
                required = true,
                validators = listOf(StateValidatorType.REQUIRED.validator)
            ),
            Field(name = "creditCardId", required = false)
        )
    ) { fields ->
        TransactionDTO(
            description = fields["description"]!!.text,
            amount = BigDecimal.valueOf(fields["amount"]!!.text.toLong()),
            category = fields["category"]!!.text,
            type = TransactionType.valueOf(fields["type"]!!.text),
            date = fields["date"]!!.text.toLong().toLocalDateTime(),
            installmentAmount = fields["installmentAmount"]!!.text.toInt(),
            creditCardId = if (fields["creditCardId"]?.text?.isNotEmpty() == true) fields["creditCardId"]?.text?.toLong() else null
        )
    }

    val isEnabled = form.isValid

    if (showDialog) {
        LoadingDialog(loading = loading.value)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEnabled) {
                        coroutine.launch {
                            showDialog = true
                            val transaction = form.value

                            if (transaction.creditCardId != null) {
                                balanceModel.saveCreditCardTransaction(transaction)
                            } else {
                                balanceModel.saveTransaction(transaction)
                            }

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
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Adicionar uma compra/conta",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 24.sp)
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        " Para adicionar uma compra/conta, preencha todas as informações obrigatórias marcadas com * (asterísco). ",
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
                    FSelectField(
                        form = form,
                        fieldName = "type",
                        label = "Tipo",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Invoice,
                        options = typeOptions
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
                        fieldName = "category",
                        label = "Category",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Category,
                        options = categoryOptions
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
                        label = "Descrição",
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
                        fieldName = "amount",
                        label = "Valor",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Dollar,
                        keyboardType = KeyboardType.Decimal
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
                        fieldName = "installmentAmount",
                        label = "Parcelas",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.Copy,
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
                    FDatePickerField(
                        form = form,
                        fieldName = "date",
                        label = "Data",
                        modifier = Modifier.fillMaxWidth()
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
                        fieldName = "creditCardId",
                        label = "Cartão de Crédito",
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = CustomIcons.Outline.CreditCard,
                        options = creditCardOptions
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.size(spacing / 2))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardFormScreenPreview() {
    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    FinanceAppTheme {
        TransactionFormScreen(balanceViewModel)
    }
}