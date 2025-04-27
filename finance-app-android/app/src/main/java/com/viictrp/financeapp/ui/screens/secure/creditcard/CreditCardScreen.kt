package com.viictrp.financeapp.ui.screens.secure.creditcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viictrp.financeapp.ui.components.CardCarousel
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.components.CustomIcons
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.extension.toFormattedYearMonth
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

data class CreditCardCarouselItem(
    private val id: String,
    private val color: String,
    private val title: String,
    private val description: String,
    private val detail: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
    override fun getDetail(): String = detail
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardScreen(
    balanceViewModel: BalanceViewModel,
    padding: PaddingValues,
    onNavigation: (Long?, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val loading by balanceViewModel.loading.collectAsState()
    val balance by balanceViewModel.currentBalance.collectAsState()
    val selectedCreditCard by balanceViewModel.selectedCreditCard.collectAsState()

    val transactions = selectedCreditCard?.invoices?.firstOrNull()?.transactions ?: emptyList()

    var carouselItems: List<CreditCardCarouselItem> = remember(balance) {
        balance?.creditCards
            ?.map { creditCard ->
                CreditCardCarouselItem(
                    id = creditCard.id.toString(),
                    title = creditCard.title,
                    description = creditCard.number,
                    color = creditCard.color,
                    detail = creditCard.invoiceClosingDay.toString()
                )
            } ?: emptyList()
    }

    PullToRefreshContainer(
        isRefreshing = loading,
        onRefresh = {
            coroutineScope.launch {
                balanceViewModel.loadBalance(YearMonth.now(), defineCurrent = true)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row {
                            Text(
                                "Seus Cartões",
                                fontWeight = FontWeight.Bold,
                                style = LocalTextStyle.current.copy(fontSize = 24.sp)
                            )
                            Text(
                                " (${carouselItems.size})",
                                fontWeight = FontWeight.Normal,
                                style = LocalTextStyle.current.copy(fontSize = 24.sp)
                            )
                        }
                    }
                }

                item {
                    CardCarousel(
                        carouselItems,
                        onPageChanged = { card ->
                            balanceViewModel.selectCreditCard(
                                card.getKey().toLong()
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = selectedCreditCard?.title ?: "",
                                fontWeight = FontWeight.Bold,
                                style = LocalTextStyle.current.copy(fontSize = 20.sp),
                            )
                            Text(
                                "Lançamentos da fatura de ${
                                    YearMonth.now().toFormattedYearMonth("MMMM")
                                }",
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable {
                                    onNavigation(
                                        selectedCreditCard?.id,
                                        SecureDestinations.INVOICE_ROUTE
                                    )
                                }) {
                                Text(
                                    "Ver faturas",
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
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                    .format(
                                        selectedCreditCard?.totalInvoiceAmount
                                            ?: BigDecimal(0.0)
                                    ),
                                fontWeight = FontWeight.Normal,
                                style = LocalTextStyle.current.copy(fontSize = 20.sp)
                            )
                        }
                    }
                }

                if (transactions.isNotEmpty()) {
                    itemsIndexed(
                        transactions,
                        key = { _, item -> item.id!! }) { index, transaction ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .animateItem()
                        ) {
                            TransactionCard(
                                transaction,
                                tag = if (transaction.isInstallment == true) "Parcela (${transaction.installmentNumber}/${transaction.installmentAmount})" else null,
                                tagColor = MaterialTheme.colorScheme.secondary.copy(alpha = .7f)
                            )
                        }
                    }
                } else {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.Asset("empty-lottie.json")
                            )
                            val progress by animateLottieCompositionAsState(
                                composition,
                                iterations = LottieConstants.IterateForever,
                                restartOnPlay = true
                            )

                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.size(230.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}