package com.viictrp.financeapp.ui.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.BottomNavigationBar
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.graph.authGraph
import com.viictrp.financeapp.ui.screens.graph.mainGraph
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import java.time.YearMonth

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentDestination != "splash" && currentDestination != "login"

    val authViewModel = hiltViewModel<AuthViewModel>()
    val balanceViewModel = hiltViewModel<BalanceViewModel>()

    LaunchedEffect(currentDestination) {
        if (currentDestination == "home" && balanceViewModel.balance.value == null) {
            balanceViewModel.loadBalance(YearMonth.now())
        }
    }

    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "auth",
            route = "root",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            authGraph(navController, authViewModel)
            mainGraph(navController, balanceViewModel, authViewModel)
        }
    }
}