package com.viictrp.financeapp.ui.screens.main.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
internal fun HomeScreenContent(
    navController: NavController,
    viewModel: BalanceViewModel,
    isRefreshing: Boolean,
    contentPadding: PaddingValues
) {
    val balance by viewModel.currentBalance.collectAsState()
    val space = Modifier.height(48.dp)

    val transactions = remember(balance) {
        balance?.let {
            it.creditCards
                .flatMap { it.invoices }
                .flatMap { it.transactions }
                .plus(it.transactions)
                .plus(it.recurringExpenses)
                .sortedByDescending { transition -> transition.id }
        } ?: emptyList()
    }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
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
                                    CustomIcons.DuoTone.Calendar,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Select Month",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            if (balance != null && !isRefreshing) {
                                NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                    .format(balance?.expenses)
                            } else {
                                "Carregando..."
                            },
                            style = LocalTextStyle.current.copy(fontSize = 28.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        balance?.let {
                            val value =
                                it.monthClosures[it.monthClosures.size - 1].expenses - it.expenses

                            Text(
                                NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                    .format(value),
                                style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                color = if (value > BigDecimal.ZERO) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.error
                            )
                            if (value > BigDecimal.ZERO) {
                                StatusChip(
                                    "diminuiu!",
                                    textColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.onTertiary
                                )
                            } else {
                                StatusChip(
                                    "aumentou!", textColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                    }
                }
            }
        }

        item {
            Spacer(modifier = space)
        }
        item {
            Text(
                "Últimas Compras",
                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            )
        }

        items(transactions.size) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                TransactionCard(
                    transactions[index],
                    "Nubank",
                    MaterialTheme.colorScheme.tertiary
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun StatusChip(text: String, backgroundColor: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(50))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = LocalTextStyle.current.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        )
    }
}