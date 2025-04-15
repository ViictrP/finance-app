package com.viictrp.financeapp.ui.screens.main.creditcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.viictrp.financeapp.ui.components.custom.form.FTextField
import com.viictrp.financeapp.ui.components.custom.form.controller.rememberFormController
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme


@Composable
fun CreditCardFormScreen(navController: NavController, balanceModel: BalanceViewModel) {

    val spacing = 48.dp

    val form = rememberFormController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
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
                        state = form.get("title"),
                        onStateChanged = { form.updateFieldState("title", it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Título do cartão",
                        tooltipMessage = "Campo obrigatório",
                        leadingIcon = CustomIcons.Outline.Description,
                        showError = true
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
                        state = form.get("description"),
                        onStateChanged = { form.updateFieldState("description", it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Descrição do cartão",
                        tooltipMessage = "Campo obrigatório",
                        leadingIcon = CustomIcons.Outline.Description,
                        showError = true
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
                        state = form.get("number"),
                        onStateChanged = { form.updateFieldState("number", it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Número do cartão",
                        tooltipMessage = "Campo obrigatório",
                        leadingIcon = CustomIcons.Outline.Number,
                        keyboardType = KeyboardType.Number,
                        showError = true
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
                        state = form.get("closingDate"),
                        onStateChanged = { form.updateFieldState("closingDate", it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Data de fechamento",
                        tooltipMessage = "Campo obrigatório",
                        leadingIcon = CustomIcons.Outline.Calendar,
                        showError = true
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
                        state = form.get("color"),
                        onStateChanged = { form.updateFieldState("color", it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Cor do cartão",
                        tooltipMessage = "Campo obrigatório",
                        leadingIcon = CustomIcons.Outline.Color,
                        showError = true
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
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { /* salvar */ },
                        enabled = form.isValid,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text("Salvar")
                    }
                }
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