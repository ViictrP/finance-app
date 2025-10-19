@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.screens.secure

import android.text.format.DateUtils
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.ui.components.ChartConfig
import com.viictrp.financeapp.ui.components.CurvedLineChart
import com.viictrp.financeapp.ui.components.CustomIcons
import com.viictrp.financeapp.ui.components.FinanceAppSurface
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.navigation.Screen
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceState
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

data class TransactionWithCreditCard(
    val transaction: TransactionDTO,
    val creditCard: CreditCardDTO?
)

@Composable
fun HomeScreen(
    padding: PaddingValues,
    onNavigation: (Long?, String) -> Unit
) {
    val viewModel = rememberBalanceViewModel()
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.deleteTransactionSuccess.collect {
            viewModel.state.first { !it.loading }
            snackbarHostState.showSnackbar(
                message = "Transação excluída com sucesso!",
                withDismissAction = true
            )
        }
    }

    HomeScreenContent(
        state = state,
        padding = padding,
        snackbarHostState = snackbarHostState,
        onRefresh = {
            viewModel.handleIntent(BalanceIntent.LoadBalance(YearMonth.now(), defineCurrent = true))
        },
        onTransactionClick = { transaction, creditCard ->
            viewModel.selectTransaction(transaction, creditCard)
            onNavigation(
                transaction.id,
                SecureDestinations.TRANSACTION_ROUTE
            )
        },
        onNavigateToBalance = {
            onNavigation(null, Screen.Balance.route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: BalanceState,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onRefresh: () -> Unit,
    onTransactionClick: (TransactionDTO, CreditCardDTO?) -> Unit,
    onNavigateToBalance: () -> Unit,
) {
    val numberFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val now = YearMonth.now()
    val today = LocalDateTime.now().toLocalDate()
    
    var selectedPeriod by remember { mutableStateOf("de Hoje") }
    var expanded by remember { mutableStateOf(false) }

    val monthClosure = state.currentBalance?.monthClosures
        ?.find {
            it.year == now.year && it.month == now.month.name.substring(0, 3)
        }

    val invoiceTransactions = state.currentBalance?.creditCards
        ?.flatMap { it.invoices }
        ?.flatMap { it.transactions }
        ?: emptyList()
    
    val allTransactions = (state.currentBalance?.transactions ?: emptyList()) + invoiceTransactions
    
    fun getTransactionsByPeriod(period: String = selectedPeriod): List<TransactionDTO> {
        return when (period) {
            "de Hoje" -> allTransactions.filter { it.date.toLocalDate() == today }
            "da Semana" -> allTransactions.filter { it.date.toLocalDate().isAfter(today.minusDays(7)) }
            else -> emptyList()
        }.sortedByDescending { it.date }
    }
    
    fun getTransactionsWithCreditCards(): List<TransactionWithCreditCard> {
        return getTransactionsByPeriod().map { transaction ->
            val creditCard = state.currentBalance?.creditCards?.find { it.id == transaction.creditCardId }
            TransactionWithCreditCard(transaction, creditCard)
        }
    }
    
    fun getTotalAmount(): BigDecimal {
        return getTransactionsByPeriod().sumOf { it.amount }
    }
    
    fun getTransactionsByDay(): Map<String, BigDecimal> {
        val formatter = DateTimeFormatter.ofPattern("dd/MM")
        return getTransactionsByPeriod("da Semana")
            .groupBy { 
                it.date.toLocalDate().format(formatter)
            }
            .toSortedMap()
            .mapValues { (_, transactions) -> 
                transactions.sumOf { it.amount } 
            }
    }

    PullToRefreshContainer(
        isRefreshing = state.loading,
        onRefresh = onRefresh,
        snackbarHostState = snackbarHostState,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        FinanceAppSurface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
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
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "gastos do mês",
                                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                        color = MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.5F
                                        )
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.clickable { onNavigateToBalance() }
                                    ) {
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
                                            CustomIcons.Filled.Calendar,
                                            modifier = Modifier.size(24.dp),
                                            contentDescription = "Select Month",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    when {
                                        state.loading -> "Carregando..."
                                        monthClosure != null -> numberFormatter.format(
                                            monthClosure.expenses
                                        )

                                        state.currentBalance != null -> numberFormatter.format(state.currentBalance.expenses)
                                        else -> ""
                                    },
                                    style = LocalTextStyle.current.copy(fontSize = 32.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.currentBalance?.let {
                                    val value =
                                        it.monthClosures.first().expenses - it.expenses

                                    Text(
                                        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                            .format(value),
                                        style = LocalTextStyle.current.copy(fontSize = 22.sp),
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
                                            "aumentou!",
                                            textColor = MaterialTheme.colorScheme.primary,
                                            backgroundColor = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "gastos por dia",
                                    style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.5F
                                    )
                                )
                                Spacer(Modifier.height(16.dp))
                                CurvedLineChart(
                                    data = getTransactionsByDay(),
                                    config = ChartConfig(
                                        pointRadius = 6.dp,
                                        labelsAndValuesTogether = true
                                    ),
                                    modifier = Modifier.height(150.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        state.lastUpdateTime?.let {
                            val formatted = DateUtils.getRelativeTimeSpanString(
                                it.toEpochMilli(),
                                System.currentTimeMillis(),
                                DateUtils.MINUTE_IN_MILLIS
                            )

                            Text(
                                "Última atualização $formatted",
                                style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(48.dp))
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Compras ",
                                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                fontWeight = FontWeight.Bold
                            )
                            Box {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { expanded = true }
                                ) {
                                    Text(
                                        text = selectedPeriod,
                                        style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Icon(
                                        CustomIcons.Filled.CaretDown,
                                        contentDescription = "Selecionar período",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("de Hoje") },
                                        onClick = {
                                            selectedPeriod = "de Hoje"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("da Semana") },
                                        onClick = {
                                            selectedPeriod = "da Semana"
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(
                            text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                .format(getTotalAmount()),
                            style = LocalTextStyle.current.copy(fontSize = 24.sp)
                        )
                    }
                }

                items(getTransactionsWithCreditCards().size) { index ->
                    val transactionWithCard = getTransactionsWithCreditCards()[index]
                    val transaction = transactionWithCard.transaction
                    val creditCard = transactionWithCard.creditCard

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        TransactionCard(
                            transaction,
                            creditCard?.title,
                            MaterialTheme.colorScheme.tertiary,
                            origin = Screen.Home.route
                        ) {
                            onTransactionClick(transaction, creditCard)
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
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = LocalTextStyle.current.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    FinanceAppTheme {
        HomeScreenContent(
            state = PreviewData.loadedState,
            padding = PaddingValues(0.dp),
            snackbarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onTransactionClick = { _, _ -> },
            onNavigateToBalance = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun HomeScreenContentLoadingPreview() {
    FinanceAppTheme {
        HomeScreenContent(
            state = PreviewData.loadingState,
            padding = PaddingValues(0.dp),
            snackbarHostState = remember { SnackbarHostState() },
            onRefresh = {},
            onTransactionClick = { _, _ -> },
            onNavigateToBalance = {}
        )
    }
}