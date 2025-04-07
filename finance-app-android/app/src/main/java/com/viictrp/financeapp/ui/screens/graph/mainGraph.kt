package com.viictrp.financeapp.ui.screens.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.main.BalanceScreen
import com.viictrp.financeapp.ui.screens.main.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.main.CreditCardScreen
import com.viictrp.financeapp.ui.screens.main.HomeScreen
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    balanceModel: BalanceViewModel,
    authModel: AuthViewModel
) {
    navigation(startDestination = "home", route = "main_graph") {
        composable("home") {
            HomeScreen(navController, balanceModel, authModel)
        }
        composable("credit_card") {
            CreditCardScreen(navController, balanceModel)
        }
        composable("balance") {
            BalanceScreen(balanceModel, authModel)
        }
        composable("credit_card_form") {
            CreditCardFormScreen(navController, balanceModel, authModel)
        }
    }
}