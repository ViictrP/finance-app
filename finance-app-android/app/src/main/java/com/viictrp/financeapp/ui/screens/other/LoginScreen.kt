package com.viictrp.financeapp.ui.screens.other

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.navigation.Screen

@Composable
fun LoginScreen(
    onNavigation: (String) -> Unit
) {
    val context = LocalContext.current
    val authViewModel = hiltViewModel<AuthViewModel>(context as ComponentActivity)

    LaunchedEffect(Unit) {
        authViewModel.loginWithGoogle { message ->
            Handler(Looper.getMainLooper()).post {
                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    onNavigation(Screen.Secure.route)
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
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        // Aqui pode entrar animação com Lottie ou logo animado, etc.
    }
}