package com.viictrp.financeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object SecureDestinations {
    const val SECURE_ROUTE = "secure"
    const val HOME_ROUTE = "secure/home"
    const val CREDIT_CARD_ROUTE = "secure/credit_card"
    const val BALANCE_ROUTE = "balance"
    const val CREDIT_CARD_FORM_ROUTE = "credit_card_form"
    const val TRANSACTION_FORM_ROUTE = "transaction_form"
    const val INVOICE_ROUTE = "invoice/{creditCardId}"
    const val TRANSACTION_ROUTE = "transaction"
    const val TRANSACTION_KEY = "transactionId"
}

object PublicDestinations {
    const val PUBLIC_ROUTE = "public"
    const val LOGIN_ROUTE = "login"
    const val SPLASH_ROUTE = "splash"
}

@Composable
fun rememberFinanceAppController(
    navController: NavHostController = rememberNavController()
): FinanceAppNavController = remember(navController) {
    FinanceAppNavController(navController)
}

@Stable
class FinanceAppNavController(
    val navController: NavHostController
) {

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToInvoice(creditCardId: Long, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${SecureDestinations.INVOICE_ROUTE}/$creditCardId?origin=$origin")
        }
    }

    fun navigateToTransaction(transactionId: Long, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${SecureDestinations.TRANSACTION_ROUTE}/$transactionId?origin=$origin")
        }
    }

    fun navigateTo(route: String, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("$route?origin=$origin")
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}