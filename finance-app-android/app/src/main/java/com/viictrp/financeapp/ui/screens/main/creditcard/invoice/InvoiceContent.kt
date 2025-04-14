package com.viictrp.financeapp.ui.screens.main.creditcard.invoice

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.application.dto.CreditCardDTO
import com.viictrp.financeapp.ui.components.CarouselCardContent
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.colorMap
import com.viictrp.financeapp.ui.components.extension.sharedCardStyle
import com.viictrp.financeapp.ui.components.extension.toFormattedYearMonth
import com.viictrp.financeapp.ui.components.extension.toLong
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.theme.Secondary
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
internal fun SharedTransitionScope.InvoiceContent(
    creditCard: CreditCardDTO?,
    padding: PaddingValues,
    balanceViewModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val spacing = 48.dp
    val invoice by balanceViewModel.selectedInvoice.collectAsState()
    val selectedYearMonth by balanceViewModel.selectedYearMonth.collectAsState()
    val loading by balanceViewModel.loading.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val transactions = invoice?.transactions ?: emptyList()

    LaunchedEffect(creditCard) {
        balanceViewModel.setInvoice(creditCard)
    }

    DisposableEffect(Unit) {
        onDispose {
            balanceViewModel.clear()
        }
    }

    LazyColumn(
        contentPadding = padding,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), Arrangement.SpaceBetween
            ) {
                with(sharedTransitionScope) {
                    val shape = MaterialTheme.shapes.medium
                    Box(
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = creditCard?.id.toString()),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedCardStyle(
                                color = colorMap[creditCard?.color] ?: Secondary,
                                shape = shape,
                                height = 180.dp
                            )
                    ) {
                        CarouselCardContent(getItem(creditCard), animatedVisibilityScope)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(spacing))
        }

        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = MaterialTheme.shapes.medium,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {
                MonthPicker(selectedYearMonth, loading) { yearMonth ->
                    balanceViewModel.updateYearMonth(yearMonth)
                    coroutineScope.launch {
                        creditCard?.let {
                            balanceViewModel.getInvoice(creditCard.id, selectedYearMonth)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(spacing / 2))
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
                        text = creditCard?.title ?: "",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 20.sp),
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${creditCard?.id}__title"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                    Text(
                        "Lançamentos da fatura de ${
                            selectedYearMonth.toLong()
                                .toFormattedYearMonth("MMMM")
                        }",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${creditCard?.id}_month"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxHeight()
                        .animateItem()
                ) {
                    invoice?.let {
                        Text(
                            text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                .format(
                                    transactions
                                        .map { it.amount }
                                        .fold(BigDecimal.ZERO) { acc, value -> acc + value }),
                            fontWeight = FontWeight.Normal,
                            style = LocalTextStyle.current.copy(fontSize = 20.sp),
                            modifier = Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${creditCard?.id}_total"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        )
                    }
                }
            }
        }

        if (!loading) {
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
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

private fun getItem(creditCard: CreditCardDTO?) = object : CarouselItem {
    override fun getKey() = creditCard?.id.toString()
    override fun getColor() = creditCard?.color ?: ""
    override fun getTitle() = creditCard?.title ?: ""
    override fun getDescription() = creditCard?.number ?: ""
    override fun getDetail() = creditCard?.invoiceClosingDay.toString()
}