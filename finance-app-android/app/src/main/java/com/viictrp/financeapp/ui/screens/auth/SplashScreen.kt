package com.viictrp.financeapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.ui.auth.AuthManager
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAuth {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }

        authViewModel.isAuthenticated
            .filterNotNull()
            .first { it }

        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "FinanceApp",
            fontSize = 32.sp,
            color = LocalContentColor.current,
            fontWeight = FontWeight.Bold
        )
        // Aqui pode entrar animação com Lottie ou logo animado, etc.
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    val authManager = AuthManager(LocalContext.current)
    val navController = rememberNavController()
    FinanceAppTheme {
        SplashScreen(
            navController,
            AuthViewModel(authManager)
        )
    }
}