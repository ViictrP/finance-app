package com.viictrp.financeapp.ui.screens.secure.creditcard.invoice

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.InvoiceScreen(
    creditCardId: String,
    balanceViewModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val coroutineScope = rememberCoroutineScope()
    val balance = balanceViewModel.balance.collectAsState()
    val loading by balanceViewModel.loading.collectAsState()

    val creditCard = remember(balance) {
        balance.value?.creditCards?.find { it.id.toString() == creditCardId }
    }


    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        PullToRefreshContainer(
            isRefreshing = loading,
            onRefresh = {
                coroutineScope.launch {
                    balanceViewModel.updateYearMonth(YearMonth.now())
                    balanceViewModel.getInvoice(creditCardId.toLong(), YearMonth.now())
                }
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                InvoiceContent(creditCard, padding, balanceViewModel, sharedTransitionScope, animatedVisibilityScope)
            }
        )
    }
}