package com.viictrp.financeapp.ui.screens.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.viictrp.financeapp.ui.screens.main.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.main.CreditCardScreen
import com.viictrp.financeapp.ui.screens.main.balance.BalanceScreen
import com.viictrp.financeapp.ui.screens.main.home.HomeScreen
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    balanceModel: BalanceViewModel
) {
    navigation(startDestination = "home", route = "main_graph") {
        composable("home") {
            HomeScreen(navController, balanceModel)
        }
        composable("credit_card") {
            CreditCardScreen(navController, balanceModel)
        }
        composable("balance") {
            BalanceScreen(balanceModel)
        }
        composable("credit_card_form") {
            CreditCardFormScreen(navController, balanceModel)
        }
    }
}