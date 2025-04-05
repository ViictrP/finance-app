package com.viictrp.financeapp.ui.screens.graph
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.viictrp.financeapp.ui.screens.auth.LoginScreen
import com.viictrp.financeapp.ui.screens.auth.SplashScreen
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel

fun NavGraphBuilder.authGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    balanceViewModel: BalanceViewModel
) {
    composable("splash") {
        SplashScreen(navController, authViewModel, balanceViewModel)
    }
    composable("login") {
        LoginScreen(navController, authViewModel)
    }
}