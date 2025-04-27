package com.viictrp.financeapp.ui.screens.secure.creditcard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.components.FinanceAppScaffold
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.screens.viewmodel.BalanceViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

data class CreditCardCarouselItem(
    private val id: String,
    private val color: String,
    private val title: String,
    private val description: String,
    private val detail: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
    override fun getDetail(): String = detail
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardScreen(
    navController: NavController,
    balanceViewModel: BalanceViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val loading by balanceViewModel.loading.collectAsState()

    FinanceAppScaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        PullToRefreshContainer(
            isRefreshing = loading,
            onRefresh = {
                coroutineScope.launch {
                    balanceViewModel.loadBalance(YearMonth.now(), defineCurrent = true)
                }
            },
            modifier = Modifier.fillMaxSize(),
            content = {
                CreditCardScreenContent(
                    navController,
                    balanceViewModel,
                    padding
                )
            }
        )

    }
}