package com.viictrp.financeapp.ui.screens.other

//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.size
//import androidx.compose.ui.unit.dp
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.animateLottieCompositionAsState
//import com.airbnb.lottie.compose.rememberLottieComposition
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
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authViewModel = hiltViewModel<AuthViewModel>(context as ComponentActivity)
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val user by authViewModel.user.collectAsState()

//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("animated-logo-lottie.json"))
//    val progress by animateLottieCompositionAsState(
//        composition,
//        iterations = 1,
//        speed = 1.0f,
//        restartOnPlay = false
//    )

    LaunchedEffect(Unit) {
        authViewModel.checkAuth()
    }

    LaunchedEffect(isAuthenticated, user) {
        if (isAuthenticated == true && user != null) {
            delay(1000)
            navController.navigate(Screen.Secure.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else if (isAuthenticated == false) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Secure.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "FinanceApp",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        // Aqui pode entrar animação com Lottie ou logo animado, etc.
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        LottieAnimation(
//            composition = composition,
//            progress = { progress },
//            modifier = Modifier.size(300.dp)
//        )
//    }
}