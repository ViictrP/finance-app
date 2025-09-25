package com.viictrp.financeapp.ui.screens.secure.balance

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceViewModel
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(padding: PaddingValues) {

    val context = LocalContext.current
    val viewModel = hiltViewModel<BalanceViewModel>(context as ComponentActivity)
    val loading by viewModel.loading.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clear()
        }
    }

    PullToRefreshContainer(
        viewModel,
        isRefreshing = loading,
        onRefresh = {
            viewModel.updateYearMonth(YearMonth.now())
            viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            BalanceScreenContent(viewModel)
        }
    )
}