package com.viictrp.financeapp.ui.screens.main.balance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    var selectedYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val coroutineScope = rememberCoroutineScope()

    var refreshing by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = refreshing,
            onRefresh = {
                coroutineScope.launch {
                    refreshing = true
                    selectedYearMonth = YearMonth.now()
                    viewModel.loadBalance(selectedYearMonth, defineCurrent = true)
                    refreshing = false
                }
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                BalanceContent(viewModel, padding)
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