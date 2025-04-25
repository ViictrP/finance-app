package com.viictrp.financeapp.ui.screens.main.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.custom.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(navController: NavController, viewModel: BalanceViewModel) {
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