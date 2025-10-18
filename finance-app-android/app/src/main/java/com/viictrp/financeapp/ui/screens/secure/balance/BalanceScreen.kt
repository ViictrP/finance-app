package com.viictrp.financeapp.ui.screens.secure.balance

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceIntent
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import kotlinx.coroutines.flow.first
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(padding: PaddingValues) {

    val viewModel = rememberBalanceViewModel()
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.deleteTransactionSuccess.collect {
            // Espera até que o estado não esteja mais carregando
            viewModel.state.first { !it.loading }
            
            // Agora, com o estado atualizado e não mais carregando, mostramos a snackbar
            snackbarHostState.showSnackbar(
                message = "Transação excluída com sucesso!",
                withDismissAction = true
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.handleIntent(BalanceIntent.Clear)
        }
    }

    PullToRefreshContainer(
        isRefreshing = state.loading,
        onRefresh = {
            viewModel.handleIntent(BalanceIntent.UpdateYearMonth(YearMonth.now()))
            viewModel.handleIntent(BalanceIntent.LoadBalance(YearMonth.now(), defineCurrent = true))
        },
        snackbarHostState = snackbarHostState,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        BalanceScreenContent(
            state = state,
            onMonthSelected = { yearMonth ->
                viewModel.handleIntent(BalanceIntent.UpdateYearMonth(yearMonth))
                viewModel.handleIntent(BalanceIntent.LoadBalance(yearMonth))
            }
        )
    }
}
