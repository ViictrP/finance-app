package com.viictrp.financeapp.ui.screens

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.auth.AuthManager
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.loginWithGoogle { message ->
            Handler(Looper.getMainLooper()).post {
                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    navController.navigate("home")
                }
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
            color = LocalContentColor.current,
            fontWeight = FontWeight.Bold
        )
        // Aqui pode entrar animação com Lottie ou logo animado, etc.
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val authManager = AuthManager(LocalContext.current)
    val navController = rememberNavController()
    FinanceAppTheme {
        LoginScreen(navController, AuthViewModel(authManager))
    }
}