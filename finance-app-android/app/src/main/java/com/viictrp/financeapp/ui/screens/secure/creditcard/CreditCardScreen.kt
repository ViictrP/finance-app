@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.viictrp.financeapp.ui.screens.secure.creditcard

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viictrp.financeapp.ui.components.CreditCardBox
import com.viictrp.financeapp.ui.components.FinanceAppSurface
import com.viictrp.financeapp.ui.components.PullToRefreshContainer
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.utils.rememberBalanceViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardScreen(
    padding: PaddingValues,
    onNavigation: (Long?, String) -> Unit
) {
    val viewModel = rememberBalanceViewModel()

    val coroutineScope = rememberCoroutineScope()

    val loading by viewModel.loading.collectAsState()
    val balance by viewModel.currentBalance.collectAsState()

    val creditCards = balance?.creditCards ?: emptyList()

    PullToRefreshContainer(
        viewModel,
        isRefreshing = loading,
        onRefresh = {
            coroutineScope.launch {
                viewModel.loadBalance(YearMonth.now(), defineCurrent = true)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        content = {
            FinanceAppSurface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(creditCards.size) { index ->
                        val creditCard = creditCards[index]

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CreditCardBox(creditCard) {
                                viewModel.selectCreditCard(creditCard)
                                onNavigation(creditCard.id, SecureDestinations.INVOICE_ROUTE)
                            }
                        }
                    }
                }
            }
        }
    )
}