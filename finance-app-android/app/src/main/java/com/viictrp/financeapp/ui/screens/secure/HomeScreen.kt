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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.MonthClosureDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.model.transaction.TransactionType
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

    val monthClosure = state.currentBalance?.monthClosures
        ?.find {
            it.year == now.year && it.month == now.month.name.substring(0, 3)
        }

    val transactions = state.currentBalance?.lastAddedTransactions
        ?.map { transaction ->
            val creditCard =
                state.currentBalance?.creditCards?.find { creditCard -> creditCard.id == transaction.creditCardId }

            TransactionWithCreditCard(transaction, creditCard)
        } ?: emptyList()

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
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 16.dp
                            ),
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

                                        state.currentBalance != null -> numberFormatter.format(state.currentBalance?.expenses)
                                        else -> ""
                                    },
                                    style = LocalTextStyle.current.copy(fontSize = 28.sp),
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
                                            "aumentou!",
                                            textColor = MaterialTheme.colorScheme.primary,
                                            backgroundColor = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

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
                    val transaction = transactions[index].transaction
                    val creditCard = transactions[index].creditCard

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
            snackbarHostState = SnackbarHostState(),
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
            snackbarHostState = SnackbarHostState(),
            onRefresh = {},
            onTransactionClick = { _, _ -> },
            onNavigateToBalance = {}
        )
    }
}

object PreviewData  {
    val sampleTransactions = listOf(
        TransactionDTO(
            id = 1,
            description = "Netflix",
            amount = BigDecimal("39.90"),
            category = "Assinaturas",
            type = TransactionType.RECURRING,
            date = LocalDateTime.now().minusDays(2),
            creditCardId = 1
        ),
        TransactionDTO(
            id = 2,
            description = "Almoço",
            amount = BigDecimal("45.50"),
            category = "Alimentação",
            type = TransactionType.DEFAULT,
            date = LocalDateTime.now().minusDays(1),
            creditCardId = 1
        ),
        TransactionDTO(
            id = 3,
            description = "Gasolina",
            amount = BigDecimal("150.00"),
            category = "Transporte",
            type = TransactionType.DEFAULT,
            date = LocalDateTime.now(),
            creditCardId = 2
        )
    )

    val sampleCreditCards = listOf(
        CreditCardDTO(
            id = 1,
            title = "Cartão Principal",
            description = "Mastercard Platinum",
            color = "BLUE",
            number = "**** 1234",
            invoiceClosingDay = 28,
            totalInvoiceAmount = BigDecimal("1500.75")
        ),
        CreditCardDTO(
            id = 2,
            title = "Cartão Secundário",
            description = "Visa Gold",
            color = "GOLD",
            number = "**** 5678",
            invoiceClosingDay = 15,
            totalInvoiceAmount = BigDecimal("850.20")
        )
    )

    val sampleMonthClosures = listOf(
        MonthClosureDTO(
            month = "JAN",
            year = YearMonth.now().year,
            total = BigDecimal("5000"),
            available = BigDecimal("2000"),
            expenses = BigDecimal("3000"),
            index = 0,
            finalUsdToBRL = BigDecimal("5.00")
        )
    )

    val sampleBalance = BalanceDTO(
        transactions = sampleTransactions,
        lastAddedTransactions = sampleTransactions.take(2),
        creditCards = sampleCreditCards,
        monthClosures = sampleMonthClosures,
        salary = BigDecimal("5000"),
        expenses = BigDecimal("2350.45"),
        available = BigDecimal("2649.55")
    )

    val loadingState = BalanceState(loading = true)

    val loadedState = BalanceState(
        loading = false,
        balance = sampleBalance,
        currentBalance = sampleBalance,
        selectedYearMonth = YearMonth.now(),
        creditCards = sampleCreditCards,
        isInitialized = true
    )
}