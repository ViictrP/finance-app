package com.viictrp.financeapp.ui.screens.main.home

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.custom.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: BalanceViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val loading by viewModel.loading.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = loading,
            onRefresh = {
                coroutineScope.launch {
                    viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
                }
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                HomeScreenContent(navController, viewModel, padding)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    val navController = rememberNavController()
    FinanceAppTheme {
        HomeScreen(navController, balanceViewModel)
    }
}