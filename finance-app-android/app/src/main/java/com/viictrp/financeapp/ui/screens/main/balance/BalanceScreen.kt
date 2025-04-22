package com.viictrp.financeapp.ui.screens.main.balance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.custom.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(viewModel: BalanceViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val loading by viewModel.loading.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = loading,
            onRefresh = {
                coroutineScope.launch {
                    viewModel.updateYearMonth(YearMonth.now())
                    viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
                }
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                BalanceScreenContent(viewModel, padding)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview() {
    FinanceAppTheme {
        BalanceScreen(BalanceViewModel(ApiService()))
    }
}