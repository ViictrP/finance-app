package com.viictrp.financeapp.ui.screens.main

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.auth.AuthManager
import com.viictrp.financeapp.ui.components.CreditCardImpactCard
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.SummaryCard
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.theme.PrimaryDark
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceScreen(viewModel: BalanceViewModel, authModel: AuthViewModel) {
    val balanceState = viewModel.balance.observeAsState()
    val balance = balanceState.value
    val user = authModel.user.observeAsState().value

    Scaffold(
        topBar = {
            Header(user?.fullName ?: "")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Extrato do mês.",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 24.sp)
                    )
                    Text(
                        "Acumulado de todas as transações e faturas...",
                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    MonthPicker { yearMonth ->
                        user?.let {
                            viewModel.loadBalance(yearMonth)
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    balance?.let {
                        SummaryCard(it.salary, it.expenses, it.available)
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        "Impacto nos gastos",
                        style = LocalTextStyle.current.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    balance?.let {
                        ExpensesCarousel(it.creditCards, it.salary)
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 100.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Compras",
                        style = LocalTextStyle.current.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    balance?.let {
                        Text(
                            "Gastos Fixos",
                            style = LocalTextStyle.current.copy(fontSize = 20.sp, fontStyle = FontStyle.Italic)
                        )
                        it.recurringExpenses.forEach { transaction ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                TransactionCard(transaction)
                            }
                        }

                        Text(
                            "Compras",
                            style = LocalTextStyle.current.copy(fontSize = 20.sp, fontStyle = FontStyle.Italic)
                        )
                        it.transactions.forEach { transaction ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                TransactionCard(transaction)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpensesCarousel(creditCards: List<CreditCardDTO>, salary: BigDecimal) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(creditCards.size) { index ->
            var creditCard = creditCards[index]
            CreditCardImpactCard(
                creditCard.title,
                DecimalFormat("#,##0.00'%'").format((creditCard.totalInvoiceAmount * BigDecimal(100)) / salary),
                NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                    .format(creditCard.totalInvoiceAmount),
                PrimaryDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview() {
    val authManager = AuthManager(LocalContext.current)

    FinanceAppTheme {
        BalanceScreen(BalanceViewModel(ApiService()), AuthViewModel(authManager))
    }
}