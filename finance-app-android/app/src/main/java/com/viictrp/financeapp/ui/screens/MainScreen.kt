package com.viictrp.financeapp.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.BottomNavigationBar
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.viewmodel.BalanceViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel = BalanceViewModel(ApiService())

    LaunchedEffect(Unit) {
        viewModel.loadBalance()
    }

    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "home"
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
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FinanceAppTheme {
        MainScreen()
    }
}