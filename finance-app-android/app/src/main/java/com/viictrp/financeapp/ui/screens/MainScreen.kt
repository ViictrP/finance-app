package com.viictrp.financeapp.ui.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.graph.authGraph
import com.viictrp.financeapp.ui.screens.graph.mainGraph
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

@Composable
fun MainScreen(authViewModel: AuthViewModel,
               balanceViewModel: BalanceViewModel) {
    val navController = rememberNavController()

    @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            authGraph(navController, authViewModel)
            mainGraph(navController, balanceViewModel, authViewModel)
        }
    }
}