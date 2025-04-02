package com.viictrp.financeapp.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.CreditCardImpactCard
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.SummaryCard
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.theme.PrimaryDark

@Composable
fun BalanceScreen() {
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
                    MonthPicker()
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    SummaryCard()
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
                    ExpensesCarousel()
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Compras",
                        style = LocalTextStyle.current.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Transactions()
                }
            }
        }
    }
}

@Composable
fun ExpensesCarousel() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { CreditCardImpactCard("Samsung", "4.99%", "R$ 1.154,57", PrimaryDark) }
        item { CreditCardImpactCard("Porto", "2.40%", "R$ 554,64", PrimaryDark) }
        item { CreditCardImpactCard("Sams Club", "1.99%", "R$ 518,34", PrimaryDark) } // Orange
    }
}

@Composable
fun Transactions() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
            TransactionCard("Seguro de Vida", "Casa", "R$ 189,37", "22/03/24")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview() {
    FinanceAppTheme {
        BalanceScreen()
    }
}