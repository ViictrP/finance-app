package com.viictrp.financeapp.ui.screens.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.viictrp.financeapp.auth.AuthManager
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.navigation.PublicDestinations
import com.viictrp.financeapp.ui.navigation.SecureDestinations
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val user by authViewModel.user.collectAsState()

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("animated-logo-lottie.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        speed = 1.0f,
        restartOnPlay = false
    )

    LaunchedEffect(Unit) {
        authViewModel.checkAuth()
    }

    LaunchedEffect(isAuthenticated, user) {
        if (isAuthenticated == true && user != null) {
            delay(1000)
            navController.navigate(SecureDestinations.SECURE_ROUTE) {
                popUpTo("splash") { inclusive = true }
            }
        } else if (isAuthenticated == false) {
            navController.navigate(PublicDestinations.LOGIN_ROUTE) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
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