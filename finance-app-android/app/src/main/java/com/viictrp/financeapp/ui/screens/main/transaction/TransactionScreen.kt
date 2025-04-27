@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.screens.main.transaction

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.components.FinanceAppSurface
import com.viictrp.financeapp.ui.components.spatialExpressiveSpring
import com.viictrp.financeapp.ui.screens.LocalNavAnimatedVisibilityScope
import com.viictrp.financeapp.ui.screens.LocalSharedTransitionScope
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

val transactionBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f
)

@Composable
fun TransactionScreen(
    transactionId: String,
    balanceViewModel: BalanceViewModel
) {

    val balance = balanceViewModel.balance.collectAsState()

    val transaction = remember(balance) {
        balance.value?.creditCards
            ?.flatMap { creditCard ->
                creditCard.invoices
                    .flatMap { it.transactions }
            }
            ?.find { it.id.toString() == transactionId }
    }

    val creditCard = remember(transaction) {
        balance.value?.creditCards
            ?.find { it.id == transaction?.creditCardId }
    }

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

    with(sharedTransitionScope) {
        FinanceAppSurface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .sharedBounds(
                        rememberSharedContentState(
                            key = transaction?.id.toString()
                        ),
                        animatedVisibilityScope,
                        clipInOverlayDuringTransition =
                            OverlayClip(RoundedCornerShape(roundedCornerAnim)),
                        boundsTransform = transactionBoundsTransform,
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                    )
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    transaction?.description ?: "",
                    style = LocalTextStyle.current.copy(fontSize = 28.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(
                                key = "title_${transactionId}"
                            ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = transactionBoundsTransform
                )
                    .wrapContentWidth()
                )
            }
        }
    }
}