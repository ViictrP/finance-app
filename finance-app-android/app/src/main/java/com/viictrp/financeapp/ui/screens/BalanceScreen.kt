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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.CreditCardImpactCard
import com.viictrp.financeapp.ui.components.Header
import com.viictrp.financeapp.ui.components.SummaryCard
import com.viictrp.financeapp.ui.components.TransactionCard

@Composable
fun BalanceScreen() {
    Scaffold(
        topBar = {
            Header("Victor Prado")
        }
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
                    Text("Extrato do mês.", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Acumulado de todas as transações e faturas...",
                        fontSize = 14.sp,
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
                        fontSize = 24.sp,
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
                    Text("Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(24.dp))
                    Transactions()
                }
            }
        }
    }
}

@Composable
fun MonthPicker() {
    val selectedDate by remember { mutableStateOf("March 2025") }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar") },
        trailingIcon = {
            IconButton(onClick = { /* Open month picker */ }) {
                Icon(Icons.Default.Clear, contentDescription = "Select Month")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.Gray,
        )
    )
}

@Composable
fun ExpensesCarousel() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { CreditCardImpactCard("Samsung", "4.99%", "R$ 1.154,57", Color.Black) }
        item { CreditCardImpactCard("Porto", "2.40%", "R$ 554,64", Color.Black) }
        item { CreditCardImpactCard("Sams Club", "", "R$ 518,34", Color.Black) } // Orange
    }
}

@Composable
fun Transactions() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 50.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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