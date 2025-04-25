package com.viictrp.financeapp.ui.screens.graph

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.viictrp.financeapp.ui.screens.main.balance.BalanceScreen
import com.viictrp.financeapp.ui.screens.main.creditcard.CreditCardFormScreen
import com.viictrp.financeapp.ui.screens.main.creditcard.CreditCardScreen
import com.viictrp.financeapp.ui.screens.main.creditcard.invoice.InvoiceScreen
import com.viictrp.financeapp.ui.screens.main.home.HomeScreen
import com.viictrp.financeapp.ui.screens.main.transaction.TransactionFormScreen
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.mainGraph(
    navController: NavController,
    balanceModel: BalanceViewModel,
    sharedTransitionScope: SharedTransitionScope
) {
    navigation(startDestination = "home", route = "main_graph") {
        composable("home") {
            sharedTransitionScope.HomeScreen(navController, balanceModel)
        }
        composable("credit_card") {
            sharedTransitionScope.CreditCardScreen(
                navController,
                balanceModel,
                sharedTransitionScope,
                this@composable
            )
        }
        composable("balance") {
            BalanceScreen(balanceModel)
        }
        composable("credit_card_form") {
            CreditCardFormScreen(balanceModel)
        }
        composable("transaction_form") {
            TransactionFormScreen(balanceModel)
        }
        composable("invoice/{creditCardId}") { backStackEntry ->
            sharedTransitionScope.InvoiceScreen(
                backStackEntry.arguments?.getString("creditCardId").toString(),
                balanceModel,
                sharedTransitionScope,
                this@composable
            )
        }
    }
}