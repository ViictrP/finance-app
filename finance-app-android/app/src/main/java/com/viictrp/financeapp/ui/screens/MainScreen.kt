package com.viictrp.financeapp.ui.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.components.custom.BottomNavigationBar
import com.viictrp.financeapp.ui.components.custom.Header
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.graph.authGraph
import com.viictrp.financeapp.ui.screens.graph.mainGraph
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import java.time.YearMonth

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val noBarsIn = listOf("splash", "login", "transaction")

    val authViewModel = hiltViewModel<AuthViewModel>()
    val balanceViewModel = hiltViewModel<BalanceViewModel>()

    val user by authViewModel.user.collectAsState()
    val balance by balanceViewModel.balance.collectAsState()
    val showBars = currentDestination != null && noBarsIn.none { route -> currentDestination.startsWith(route.substringBefore("/{")) }

    LaunchedEffect(currentDestination) {
        if (currentDestination == "home" && balance == null) {
            balanceViewModel.loadBalance(YearMonth.now(), defineCurrent = true)
        }
    }

    Scaffold(
        topBar = {
            if (showBars) {
                Header(user)
            }
        },
        bottomBar = {
            if (showBars) {
                BottomNavigationBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = "auth",
                route = "root",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = Modifier.padding(padding)
            ) {
                authGraph(navController, authViewModel)
                mainGraph(navController, balanceViewModel, this@SharedTransitionLayout)
            }
        }
    }
}