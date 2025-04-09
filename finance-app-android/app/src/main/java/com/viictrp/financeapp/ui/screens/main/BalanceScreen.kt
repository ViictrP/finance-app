package com.viictrp.financeapp.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(viewModel: BalanceViewModel, authModel: AuthViewModel) {
    val balance by viewModel.balance.collectAsState()
    val user by authModel.user.collectAsState()
    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val coroutineScope = rememberCoroutineScope()

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    val recurringExpenses = remember(balance) {
        balance?.recurringExpenses ?: emptyList()
    }

    val transactions = remember(balance) {
        balance?.transactions ?: emptyList()
    }

    val spacing = Modifier.height(48.dp)

    Scaffold(
        topBar = {
            Header(user?.fullName ?: "")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                coroutineScope.launch {
                    refreshing = true
                    selectedYearMonth = YearMonth.now()
                    viewModel.loadBalance(selectedYearMonth, defineCurrent = true)
                    refreshing = false
                }
            },
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = refreshing,
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullRefreshState
                )
            },
            state = pullRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 24.dp)
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
                    Spacer(modifier = spacing)
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        MonthPicker(selectedYearMonth) { yearMonth ->
                            selectedYearMonth = yearMonth
                            user?.let {
                                coroutineScope.launch {
                                    viewModel.loadBalance(selectedYearMonth)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = spacing)
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
                    Spacer(modifier = spacing)
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
                    Spacer(modifier = spacing)
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            "Compras",
                            style = LocalTextStyle.current.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    Text(
                        "Gastos Fixos",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp),
                        style = LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
                    )
                }

                items(recurringExpenses.size) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        TransactionCard(recurringExpenses[index])
                    }
                }

                item {
                    Spacer(modifier = spacing)
                }

                item {
                    Text(
                        "Compras",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp),
                        style = LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
                    )
                }

                items(transactions.size) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        TransactionCard(transactions[index])
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
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