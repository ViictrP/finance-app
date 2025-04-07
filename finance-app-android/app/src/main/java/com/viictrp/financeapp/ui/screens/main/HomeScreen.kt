package com.viictrp.financeapp.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.auth.AuthManager
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModelFactory
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewModel: BalanceViewModel, authModel: AuthViewModel) {
    val balanceState = viewModel.balance.observeAsState()
    val balance = balanceState.value
    val userState = authModel.user.observeAsState()
    val user = userState.value

    LaunchedEffect(Unit) {
        user?.let {
            viewModel.loadBalance(YearMonth.now())
        }
    }

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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "gastos do mês",
                                    style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.clickable { navController.navigate("balance") }) {
                                    Text(
                                        SimpleDateFormat(
                                            "MMMM",
                                            Locale.getDefault()
                                        ).format(Calendar.getInstance().time),
                                        style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Icon(
                                        Icons.Outlined.DateRange,
                                        modifier = Modifier.size(24.dp),
                                        contentDescription = "Select Month",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                balance?.let {
                                    NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                        .format(it.salary)
                                } ?: "Carregando...",
                                style = LocalTextStyle.current.copy(fontSize = 28.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            balance?.let {
                                val value =it.expenses.subtract(it.monthClosures[it.monthClosures.size - 2].expenses)

                                Text(
                                    NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                        .format(value),
                                    style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                if (value > BigDecimal.ZERO) {
                                    Text(
                                        "diminuiu!",
                                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                } else {
                                    Text(
                                        "aumentou!",
                                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                        }
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
                            style = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        it.recurringExpenses.forEach { transaction ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                TransactionCard(transaction)
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            "Compras",
                            style = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthManager(LocalContext.current))
    )

    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    val navController = rememberNavController()
    FinanceAppTheme {
        HomeScreen(navController, balanceViewModel, authViewModel)
    }
}