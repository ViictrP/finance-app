package com.viictrp.financeapp.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.BottomNavigationBar
import com.viictrp.financeapp.ui.navigation.mainGraph
import com.viictrp.financeapp.ui.screens.graph.authGraph
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

@Composable
fun MainScreen(viewModel: BalanceViewModel, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val showBottomBar = currentRoute !in listOf("login", "splash")

    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            authGraph(navController, authViewModel, viewModel)
            mainGraph(navController, viewModel)
        }
    }
}