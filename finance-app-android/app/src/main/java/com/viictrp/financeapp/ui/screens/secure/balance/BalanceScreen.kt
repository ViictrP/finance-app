package com.viictrp.financeapp.ui.screens.secure.balance

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(padding: PaddingValues) {

    val viewModel = rememberBalanceViewModel()
    
    // ✅ FULL MVI - Apenas state
    val state by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            // ✅ MVI - Usando handleIntent
            viewModel.handleIntent(BalanceIntent.Clear)
        }
    }

    PullToRefreshContainer(
        viewModel,
        isRefreshing = state.loading,
        onRefresh = {
            // ✅ MVI - Usando handleIntent
            viewModel.handleIntent(BalanceIntent.UpdateYearMonth(YearMonth.now()))
            viewModel.handleIntent(BalanceIntent.LoadBalance(YearMonth.now(), defineCurrent = true))
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            BalanceScreenContent(viewModel)
        }
    )
}