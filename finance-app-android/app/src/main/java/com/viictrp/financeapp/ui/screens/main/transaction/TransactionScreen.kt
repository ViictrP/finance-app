package com.viictrp.financeapp.ui.screens.main.transaction

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TransactionScreen(
    transactionId: String,
    balanceViewModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
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

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .sharedElement(
                    state = rememberSharedContentState(transactionId),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ -> tween(durationMillis = 300) }
                )
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            transaction?.let {
                TransactionCard(it)
            }
        }
    }
}