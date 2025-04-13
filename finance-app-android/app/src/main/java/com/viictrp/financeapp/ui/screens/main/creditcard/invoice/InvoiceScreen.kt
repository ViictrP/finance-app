package com.viictrp.financeapp.ui.screens.main.creditcard.invoice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.viictrp.financeapp.ui.components.custom.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(creditCardId: String, balanceViewModel: BalanceViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val balance = balanceViewModel.balance.collectAsState()

    val creditCard = remember(balance) {
        balance.value?.creditCards?.find { it.id.toString() == creditCardId }
    }

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        PullToRefreshContainer(
            isRefreshing = refreshing,
            onRefresh = {
                coroutineScope.launch {
                    refreshing = true
                    balanceViewModel.getInvoice(creditCardId.toLong(), YearMonth.now())
                    refreshing = false
                }
            },
            state = pullRefreshState,
            modifier = Modifier.fillMaxSize(),
            content = {
                InvoiceContent(creditCard, refreshing, padding, balanceViewModel)
            }
        )
    }
}