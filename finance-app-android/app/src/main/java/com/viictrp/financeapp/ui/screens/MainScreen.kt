package com.viictrp.financeapp.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.BottomNavigationBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("credit_card") {
                CreditCardScreen(navController)
            }
            composable("balance") {
                BalanceScreen()
            }
            composable("credit_card_form") {
                CreditCardFormScreen(navController)
            }
        }
    }
}