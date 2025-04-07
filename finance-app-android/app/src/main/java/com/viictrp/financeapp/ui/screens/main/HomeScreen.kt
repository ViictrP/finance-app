package com.viictrp.financeapp.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
            if (balance == null) {
                viewModel.loadBalance(YearMonth.now())
            }
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
                                        .format(it.expenses)
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
                                    color = if (value > BigDecimal.ZERO) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.error
                                )
                                if (value > BigDecimal.ZERO) {
                                    StatusChip("diminuiu!", MaterialTheme.colorScheme.onTertiary)
                                } else {
                                    StatusChip("aumentou!", MaterialTheme.colorScheme.error)
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
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 100.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Últimas Compras",
                        style = LocalTextStyle.current.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    balance?.let {
                        it.creditCards
                            .flatMap { it.invoices }
                            .flatMap { it.transactions }
                            .plus(it.transactions)
                            .plus(it.recurringExpenses)
                            .sortedByDescending { transition -> transition.id }
                            .forEach { transaction ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    TransactionCard(transaction, "Nubank", MaterialTheme.colorScheme.tertiary)
                                }
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(text: String, backgroundColor: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = LocalTextStyle.current.copy(fontSize = 10.sp)
        )
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