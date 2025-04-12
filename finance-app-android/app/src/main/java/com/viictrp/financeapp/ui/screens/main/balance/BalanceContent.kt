package com.viictrp.financeapp.ui.screens.main.balance

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.ui.components.CreditCardImpactCard
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.SummaryCard
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.theme.PrimaryDark
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BalanceContent(viewModel: BalanceViewModel, contentPadding: PaddingValues) {
    val balance by viewModel.balance.collectAsState()
    val spacing = Modifier.height(48.dp)

    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val coroutineScope = rememberCoroutineScope()

    val recurringExpenses = remember(balance) {
        balance?.recurringExpenses ?: emptyList()
    }

    val transactions = remember(balance) {
        balance?.transactions ?: emptyList()
    }

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
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

        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                MonthPicker(selectedYearMonth) { yearMonth ->
                    selectedYearMonth = yearMonth
                    coroutineScope.launch {
                        viewModel.loadBalance(selectedYearMonth)
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
                    .animateItem()
            ) {
                TransactionCard(transactions[index])
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
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