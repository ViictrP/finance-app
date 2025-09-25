package com.viictrp.financeapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Secure : Screen("secure")

    object Home : Screen("secure/home")
    object Balance : Screen("secure/balance?origin={origin}")
    object CreditCard : Screen("secure/credit_card")
    object CreditCardForm : Screen("secure/credit_card_form")
    object TransactionForm : Screen("secure/transaction_form")

    data class Transaction(val transactionId: Long, val origin: String) : Screen(
        "secure/transaction/{transactionId}?origin={origin}"
    ) {
        fun buildRoute() = "secure/transaction/$transactionId?origin=$origin"
    }

    data class Invoice(val creditCardId: Long, val origin: String) : Screen(
        "secure/invoice/{creditCardId}?origin={origin}"
    ) {
        fun buildRoute() = "secure/invoice/$creditCardId?origin=$origin"
    }
}
