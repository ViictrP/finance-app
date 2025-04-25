package com.viictrp.financeapp.ui.screens.main.creditcard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viictrp.financeapp.ui.components.CardCarousel
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.extension.toFormattedYearMonth
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CreditCardScreenContent(
    navController: NavController,
    balanceViewModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    padding: PaddingValues
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

    with(sharedTransitionScope) {
        key(balance) {
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
                                    boundsTransform = { _, _ ->
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
                                    boundsTransform = { _, _ ->
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
                                    CustomIcons.Filled.Calendar,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = "Select Month",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                )
                            }
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                    .format(
                                        selectedCreditCard?.totalInvoiceAmount ?: BigDecimal(0.0)
                                    ),
                                fontWeight = FontWeight.Normal,
                                style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "${selectedCreditCard?.id}_total"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = { _, _ ->
                                            tween(durationMillis = 200)
                                        }
                                    )
                            )
                        }
                    }
                }

                if (transactions.isNotEmpty()) {
                    itemsIndexed(transactions, key = { _, item -> item.id!! }) { index, transaction ->
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
    }
}