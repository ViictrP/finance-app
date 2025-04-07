package com.viictrp.financeapp.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewModel: BalanceViewModel) {
    val balanceState = viewModel.balance.observeAsState()
    val balance = balanceState.value

    Scaffold(
        topBar = {
            Header("Victor Prado")
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
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
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
                                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(it.salary)
                            } ?: "Carregando...",
                            style = LocalTextStyle.current.copy(fontSize = 40.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    FinanceAppTheme {
        HomeScreen(navController, BalanceViewModel(ApiService()))
    }
}