@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.screens.secure.transaction

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.TransactionCardSharedElementKey
import com.viictrp.financeapp.ui.components.TransactionCardSharedElementType
import com.viictrp.financeapp.ui.components.animation.boundsTransform
import com.viictrp.financeapp.ui.components.extension.toFormatted
import com.viictrp.financeapp.ui.components.nonSpatialExpressiveSpring
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionScreen(
    transactionId: Long,
    origin: String,
    onPressUp: (() -> Unit)? = null
) {
    val viewModel = rememberBalanceViewModel()

    val numberFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    
    // ✅ FULL MVI - Apenas state
    val state by viewModel.state.collectAsState()
    val transaction = state.selectedTransaction

    val installments = state.installments
    val loading = state.loading

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val roundedCornerAnim by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 16.dp
                EnterExitState.Visible -> 0.dp
                EnterExitState.PostExit -> 16.dp
            }
        }

    LaunchedEffect(transaction) {
        if (transaction?.isInstallment == true) {
            viewModel.handleIntent(BalanceIntent.LoadInstallments(transaction.id!!, transaction.installmentId!!))
        }
    }

    with(sharedTransitionScope) {
        FinanceAppSurface(
            modifier = Modifier
                .sharedBounds(
                    rememberSharedContentState(
                        key = TransactionCardSharedElementKey(
                            transactionId = transactionId,
                            origin = origin,
                            type = TransactionCardSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope,
                    clipInOverlayDuringTransition =
                        OverlayClip(RoundedCornerShape(roundedCornerAnim)),
                    boundsTransform = boundsTransform,
                    exit = fadeOut(nonSpatialExpressiveSpring()),
                    enter = fadeIn(nonSpatialExpressiveSpring()),
                )
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.BottomStart,
                        ) {
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                BackButton {
                                    onPressUp?.invoke()
                                }

                                state.selectedCreditCard?.let {
                                    Text(
                                        it.title,
                                        style = LocalTextStyle.current.copy(fontSize = 40.sp),
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f),
                                        modifier = Modifier
                                            .sharedBounds(
                                                rememberSharedContentState(
                                                    key = CreditCardSharedKey(
                                                        creditCardId = it.id!!,
                                                        type = CreditCardSharedKeyElementType.Title
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

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${transaction?.date?.toFormatted()}",
                                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f),
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(
                                    transaction?.description!!,
                                    style = LocalTextStyle.current.copy(fontSize = 30.sp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .sharedBounds(
                                            rememberSharedContentState(
                                                key = TransactionCardSharedElementKey(
                                                    transactionId = transactionId,
                                                    origin = origin,
                                                    type = TransactionCardSharedElementType.Title
                                                )
                                            ),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            boundsTransform = boundsTransform
                                        )
                                        .wrapContentWidth()
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            transaction.id?.let { transactionId ->
                                                // ✅ MVI - Usando handleIntent
                                                viewModel.handleIntent(BalanceIntent.DeleteTransaction(transactionId, transaction.isInstallment!!))
                                                onPressUp?.invoke()
                                            }
                                        }) {
                                        Icon(
                                            CustomIcons.Filled.TrashCan,
                                            contentDescription = "Delete Transaction",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                with(animatedVisibilityScope) {
                                    val installmentAmount = transaction?.installmentAmount?.takeIf {
                                        it > 1
                                    }
                                    if (installmentAmount != null) {
                                        Text(
                                            "parcela (${transaction.installmentNumber}/${installmentAmount})",
                                            style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                            color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f),
                                            modifier = Modifier.animateEnterExit(
                                                enter = fadeIn(
                                                    animationSpec = tween(
                                                        durationMillis = 200,
                                                        delayMillis = 200
                                                    )
                                                ) +
                                                        slideInVertically(
                                                            animationSpec = tween(
                                                                durationMillis = 300,
                                                                delayMillis = 200
                                                            )
                                                        ) { -it },
                                                exit = fadeOut(animationSpec = tween(durationMillis = 200)) +
                                                        slideOutVertically(
                                                            animationSpec = tween(durationMillis = 150)
                                                        ) { -it }
                                            )
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    numberFormatter.format(transaction?.amount ?: 0),
                                    style = LocalTextStyle.current.copy(fontSize = 24.sp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }

                    itemsIndexed(
                        installments,
                        key = { _, item -> item!!.id!! }) { index, transaction ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .animateItem()
                        ) {
                            TransactionCard(
                                transaction!!,
                                tag = if (transaction.isInstallment == true) "Parcela (${transaction.installmentNumber}/${transaction.installmentAmount})" else null,
                                tagColor = MaterialTheme.colorScheme.secondary.copy(
                                    alpha = .7f
                                ),
                                origin = SecureDestinations.CREDIT_CARD_ROUTE
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(48.dp))
                    }

                    @SuppressLint("UnusedCrossfadeTargetStateParameter")
                    item {
                        if (loading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Crossfade(targetState = true, label = "lottieFade") { _ ->
                                    val composition by rememberLottieComposition(
                                        LottieCompositionSpec.Asset("loading-lottie.json")
                                    )
                                    val progress by animateLottieCompositionAsState(
                                        composition,
                                        iterations = LottieConstants.IterateForever,
                                        restartOnPlay = true
                                    )

                                    LottieAnimation(
                                        composition = composition,
                                        progress = { progress },
                                        modifier = Modifier.size(200.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
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