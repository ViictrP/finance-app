@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)

package com.viictrp.financeapp.ui.screens.secure.creditcard

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viictrp.financeapp.ui.components.CreditCardSharedKey
import com.viictrp.financeapp.ui.components.CreditCardSharedKeyElementType
import com.viictrp.financeapp.ui.components.CustomIcons
import com.viictrp.financeapp.ui.components.FinanceAppSurface
import com.viictrp.financeapp.ui.components.MonthPicker
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.animation.boundsTransform
import com.viictrp.financeapp.ui.components.colorMap
import com.viictrp.financeapp.ui.components.nonSpatialExpressiveSpring
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.theme.Background
import com.viictrp.financeapp.ui.theme.Secondary
import com.viictrp.financeapp.ui.theme.SecondaryDark
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun InvoiceScreen(
    creditCardId: Long,
    onPressUp: (() -> Unit)? = null,
    onNavigation: ((id: Long, destination: String) -> Unit)? = null,
) {
    val viewModel = rememberBalanceViewModel()

    val coroutineScope = rememberCoroutineScope()
    val loading by viewModel.loading.collectAsState()

    val spacing = 48.dp
    val creditCard = viewModel.selectedCreditCard.collectAsState()
    val invoice by viewModel.selectedInvoice.collectAsState()
    val selectedYearMonth by viewModel.selectedYearMonth.collectAsState()

    val transactions = invoice?.transactions ?: emptyList()

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")
    val roundedCornerAnimation by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 0.dp
                EnterExitState.Visible -> 16.dp
                EnterExitState.PostExit -> 16.dp
            }
        }

    val animatedFontSize by animatedVisibilityScope.transition.animateDp(label = "Title Font Size") { state ->
        when (state) {
            EnterExitState.PreEnter -> 24.dp
            EnterExitState.Visible -> 48.dp
            EnterExitState.PostExit -> 24.dp
        }
    }

    val density = LocalDensity.current
    val animatedFontSizeSp = with(density) { animatedFontSize.toSp() }

    LaunchedEffect(creditCardId) {
        viewModel.updateYearMonth(YearMonth.now())
    }

    with(sharedTransitionScope) {
        PullToRefreshContainer(
            viewModel = viewModel,
            isRefreshing = loading,
            onRefresh = {
                coroutineScope.launch {
                    viewModel.updateYearMonth(YearMonth.now())
                    viewModel.getInvoice(creditCardId, YearMonth.now())
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            content = {
                val color = colorMap[creditCard.value?.color] ?: Secondary
                FinanceAppSurface(
                    color = color,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = CreditCardSharedKey(
                                    creditCardId = creditCard.value!!.id!!,
                                    type = CreditCardSharedKeyElementType.Bounds
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = boundsTransform,
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(
                                    roundedCornerAnimation
                                )
                            ),
                            enter = fadeIn(nonSpatialExpressiveSpring()),
                            exit = fadeOut(nonSpatialExpressiveSpring())
                        )
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(top = 16.dp),
                        modifier = Modifier.skipToLookaheadSize()
                    ) {
                        item {
                            BackButton {
                                onPressUp?.invoke()
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = creditCard.value?.title!!,
                                        color = SecondaryDark,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = animatedFontSizeSp
                                        ),
                                        modifier = Modifier
                                            .sharedBounds(
                                                rememberSharedContentState(
                                                    key = CreditCardSharedKey(
                                                        creditCardId = creditCard.value?.id!!,
                                                        type = CreditCardSharedKeyElementType.Title
                                                    )
                                                ),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = boundsTransform
                                            )
                                            .wrapContentWidth()
                                    )
                                    IconButton(onClick = {}) {
                                        Icon(
                                            CustomIcons.Filled.Settings,
                                            modifier = Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(
                                                        key = CreditCardSharedKey(
                                                            creditCardId = creditCard.value?.id!!,
                                                            type = CreditCardSharedKeyElementType.Actions
                                                        )
                                                    ),
                                                    animatedVisibilityScope = animatedVisibilityScope,
                                                    boundsTransform = boundsTransform
                                                )
                                                .wrapContentWidth()
                                                .size(24.dp),
                                            contentDescription = "Select Month",
                                            tint = Color.White,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.size(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            CustomIcons.Filled.Calendar,
                                            modifier = Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(
                                                        key = CreditCardSharedKey(
                                                            creditCardId = creditCard.value?.id!!,
                                                            type = CreditCardSharedKeyElementType.Icon
                                                        )
                                                    ),
                                                    animatedVisibilityScope = animatedVisibilityScope,
                                                    boundsTransform = boundsTransform
                                                )
                                                .wrapContentWidth()
                                                .size(24.dp),
                                            contentDescription = "Select Month",
                                            tint = SecondaryDark,
                                        )
                                        Text(
                                            text = " ${creditCard.value?.invoiceClosingDay}",
                                            color = SecondaryDark,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 24.sp
                                            ),
                                            modifier = Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(
                                                        key = CreditCardSharedKey(
                                                            creditCardId = creditCard.value?.id!!,
                                                            type = CreditCardSharedKeyElementType.InvoiceClosingDay
                                                        )
                                                    ),
                                                    animatedVisibilityScope = animatedVisibilityScope,
                                                    boundsTransform = boundsTransform
                                                )
                                                .wrapContentWidth()
                                        )
                                    }
                                    Text(
                                        text = creditCard.value?.number!!,
                                        color = SecondaryDark,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .sharedBounds(
                                                rememberSharedContentState(
                                                    key = CreditCardSharedKey(
                                                        creditCardId = creditCard.value?.id!!,
                                                        type = CreditCardSharedKeyElementType.Number
                                                    )
                                                ),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = boundsTransform
                                            )
                                            .wrapContentWidth()
                                    )
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
                                    .background(
                                        colorMap[creditCard.value?.color!!] ?: Background
                                    )
                                    .padding(horizontal = 16.dp)
                                    .skipToLookaheadSize()
                            ) {
                                MonthPicker(selectedYearMonth, loading) { yearMonth ->
                                    viewModel.updateYearMonth(yearMonth)
                                    coroutineScope.launch {
                                        viewModel.getInvoice(
                                            creditCard.value?.id!!,
                                            selectedYearMonth
                                        )
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
                                        text = creditCard.value?.title!!,
                                        fontWeight = FontWeight.Bold,
                                        style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                        color = SecondaryDark
                                    )
                                    Text(
                                        "Lançamentos da fatura",
                                        fontWeight = FontWeight.Normal,
                                        color = SecondaryDark
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                ) {
                                    invoice?.let {
                                        Text(
                                            text = NumberFormat.getCurrencyInstance(
                                                Locale(
                                                    "pt",
                                                    "BR"
                                                )
                                            )
                                                .format(
                                                    transactions
                                                        .map { it.amount }
                                                        .fold(BigDecimal.ZERO) { acc, value -> acc + value }),
                                            fontWeight = FontWeight.Normal,
                                            style = LocalTextStyle.current.copy(fontSize = 20.sp),
                                            color = SecondaryDark,
                                            modifier = Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(
                                                        key = CreditCardSharedKey(
                                                            creditCardId = creditCard.value?.id!!,
                                                            type = CreditCardSharedKeyElementType.InvoiceTotalAmount
                                                        )
                                                    ),
                                                    animatedVisibilityScope = animatedVisibilityScope,
                                                    boundsTransform = boundsTransform
                                                )
                                                .wrapContentWidth()
                                        )
                                    }
                                }
                            }
                        }

                        if (!loading) {
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
                                            tagColor = MaterialTheme.colorScheme.secondary.copy(
                                                alpha = .7f
                                            ),
                                            origin = SecureDestinations.INVOICE_ROUTE
                                        ) { id ->
                                            viewModel.selectTransaction(transaction, creditCard.value)
                                            onNavigation?.invoke(
                                                id,
                                                SecureDestinations.INVOICE_ROUTE
                                            )
                                        }
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
        )
    }
}

@Composable
private fun SharedTransitionScope.BackButton(upPress: () -> Unit) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")
    with(animatedVisibilityScope) {
        IconButton(
            onClick = upPress,
            modifier = Modifier
                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 3f)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .size(36.dp)
                .animateEnterExit(
                    enter = scaleIn(tween(300, delayMillis = 200)),
                    exit = scaleOut(tween(20))
                )
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = .8f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "test"
            )
        }
    }
}