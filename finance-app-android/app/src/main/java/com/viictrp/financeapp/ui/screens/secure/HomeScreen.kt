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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.ui.components.CustomIcons
import com.viictrp.financeapp.ui.components.FinanceAppSurface
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.text.format

data class TransactionWithCreditCard(
    val transaction: TransactionDTO,
    val creditCard: CreditCardDTO?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BalanceViewModel,
    padding: PaddingValues,
    onNavigation: (Long?, String) -> Unit
) {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val lastUpdateTime by viewModel.lastUpdateTime.collectAsState()
    val balance by viewModel.currentBalance.collectAsState()
    val space = Modifier.height(48.dp)

    val transactions = balance?.lastAddedTransactions
        ?.map { transaction ->
            val creditCard =
                balance?.creditCards?.find { creditCard -> creditCard.id == transaction.creditCardId }

            TransactionWithCreditCard(transaction, creditCard)
        } ?: emptyList()

    val coroutineScope = rememberCoroutineScope()
    val loading by viewModel.loading.collectAsState()

    PullToRefreshContainer(
        isRefreshing = loading,
        onRefresh = {
            coroutineScope.launch {
                viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            FinanceAppSurface(modifier = Modifier
                .fillMaxSize()) {
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
                                            modifier = Modifier.clickable {
                                                onNavigation(
                                                    null,
                                                    SecureDestinations.BALANCE_ROUTE
                                                )
                                            }) {
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
                                        if (balance != null && !loading) {
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
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
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
                            lastUpdateTime?.let {
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
                                origin = SecureDestinations.HOME_ROUTE
                            ) { id ->
                                viewModel.selectTransaction(transaction, creditCard)
                                onNavigation(
                                    id,
                                    SecureDestinations.TRANSACTION_ROUTE
                                )
                            }
                        }
                    }
                }
            }
        }
    )
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