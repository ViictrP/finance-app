package com.viictrp.financeapp.ui.screens.secure.balance

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.domain.model.transaction.TransactionType
import com.viictrp.financeapp.ui.components.CreditCardImpactCard
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.SummaryCard
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import com.viictrp.financeapp.ui.theme.PrimaryDark
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BalanceScreenContent(
    balanceViewModel: BalanceViewModel,
) {
    val spacing = Modifier.height(48.dp)
    
    // ✅ FULL MVI - Apenas state
    val state by balanceViewModel.state.collectAsState()

    val monthClosure = state.balance?.monthClosures
        ?.find {
            it.year == state.selectedYearMonth.year && it.month == state.selectedYearMonth.month.name.substring(0, 3)
        }

    val recurringExpenses = state.balance?.transactions
        ?.filter { transaction -> transaction.type == TransactionType.RECURRING } ?: emptyList()

    val transactions =
        state.balance?.transactions?.filter { transaction -> transaction.type == TransactionType.DEFAULT }
            ?: emptyList()

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
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
                MonthPicker(state.selectedYearMonth, state.loading) { yearMonth ->
                    // ✅ MVI - Usando handleIntent
                    balanceViewModel.handleIntent(BalanceIntent.UpdateYearMonth(yearMonth))
                    balanceViewModel.handleIntent(BalanceIntent.LoadBalance(yearMonth))
                }
            }
        }

        if (!state.loading) {
            item {
                Spacer(modifier = spacing)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    when {
                        monthClosure != null -> SummaryCard(monthClosure.total, monthClosure.expenses, monthClosure.available)
                        state.balance != null -> SummaryCard(state.balance!!.salary, state.balance!!.expenses, state.balance!!.available)
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
                    state.balance?.let {
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
                    TransactionCard(
                        recurringExpenses[index],
                        origin = SecureDestinations.BALANCE_ROUTE,
                    )
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
                    TransactionCard(transactions[index], origin = SecureDestinations.BALANCE_ROUTE)
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
            val creditCard = creditCards[index]
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