package com.viictrp.financeapp.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.viictrp.financeapp.ui.screens.main.BalanceScreen
import com.viictrp.financeapp.ui.screens.main.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.main.CreditCardScreen
import com.viictrp.financeapp.ui.screens.main.HomeScreen
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    viewModel: BalanceViewModel
) {
    composable("home") {
        HomeScreen(navController, viewModel)
    }
    composable("credit_card") {
        CreditCardScreen(navController)
    }
    composable("balance") {
        BalanceScreen(viewModel)
    }
    composable("credit_card_form") {
        CreditCardFormScreen(navController)
    }
}