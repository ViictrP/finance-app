package com.viictrp.financeapp.ui.screens.main.creditcard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.CardCarousel
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.extension.toFormattedYearMonth
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CreditCardScreen(
    navController: NavController,
    balanceViewModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
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
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        with(sharedTransitionScope) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = padding
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
                        },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
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
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "${selectedCreditCard?.id}__title"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = {_,_ ->
                                        tween(durationMillis = 200)
                                    }
                                )
                            )
                            Text(
                                "Lançamentos da fatura de ${
                                    YearMonth.now().toFormattedYearMonth("MMMM")
                                }",
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "${selectedCreditCard?.id}_month"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = {_,_ ->
                                        tween(durationMillis = 200)
                                    }
                                )
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable { navController.navigate("invoice/${selectedCreditCard?.id}") }) {
                                Text(
                                    "Ver faturas",
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
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                    .format(selectedCreditCard?.totalInvoiceAmount ?: BigDecimal(0.0)),
                                fontWeight = FontWeight.Normal,
                                style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "${selectedCreditCard?.id}_total"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = {_,_ ->
                                            tween(durationMillis = 200)
                                        }
                                    )
                            )
                        }
                    }
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
    }
}