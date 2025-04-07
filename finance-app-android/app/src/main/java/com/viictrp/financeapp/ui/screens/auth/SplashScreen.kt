package com.viictrp.financeapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import kotlinx.coroutines.delay
import java.time.YearMonth

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    balanceViewModel: BalanceViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAuth()
        authViewModel.isAuthenticated.collect { loggedIn ->
            delay(1000)
            if (loggedIn == true) {
                balanceViewModel.loadBalance(YearMonth.now())
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "FinanceApp",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        // Aqui pode entrar animação com Lottie ou logo animado, etc.
    }
}