package com.viictrp.financeapp.ui.screens.graph
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.viictrp.financeapp.ui.screens.auth.LoginScreen
import com.viictrp.financeapp.ui.screens.auth.SplashScreen
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel

fun NavGraphBuilder.authGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    composable("splash") {
        SplashScreen(navController, authViewModel)
    }
    composable("login") {
        LoginScreen(navController, authViewModel)
    }
}