package com.viictrp.financeapp.ui.screens.main.creditcard

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.custom.form.FSelectField
import com.viictrp.financeapp.ui.components.custom.form.FTextField
import com.viictrp.financeapp.ui.components.custom.form.controller.Field
import com.viictrp.financeapp.ui.components.custom.form.controller.StateValidator
import com.viictrp.financeapp.ui.components.custom.form.controller.StateValidatorType
import com.viictrp.financeapp.ui.components.custom.form.controller.rememberFormController
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme


@Composable
fun CreditCardFormScreen(navController: NavController, balanceModel: BalanceViewModel) {

    val spacing = 48.dp

    val form = rememberFormController(
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
                "closingDate", required = true, validators = listOf(
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
    )

    val isEnabled = form.isValid

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isEnabled) {
                        Log.d("CreditCardFormScreen", "form.value: ${form.value}")
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                        fieldName = "closingDate",
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
                        options = listOf("Black", "Orange", "Blue", "Purple")
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
    val navController = rememberNavController()
    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    FinanceAppTheme {
        CreditCardFormScreen(navController, balanceViewModel)
    }
}