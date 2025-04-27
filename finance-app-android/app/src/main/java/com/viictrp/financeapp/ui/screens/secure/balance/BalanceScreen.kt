package com.viictrp.financeapp.ui.screens.secure.balance

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(viewModel: BalanceViewModel, padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()

    val loading by viewModel.loading.collectAsState()

    PullToRefreshContainer(
        isRefreshing = loading,
        onRefresh = {
            coroutineScope.launch {
                viewModel.updateYearMonth(YearMonth.now())
                viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            BalanceScreenContent(viewModel)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview() {
    FinanceAppTheme {
        BalanceScreen(BalanceViewModel(ApiService()), PaddingValues())
    }
}