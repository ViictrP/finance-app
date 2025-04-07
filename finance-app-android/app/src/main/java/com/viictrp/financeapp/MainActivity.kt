package com.viictrp.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.auth.AuthManager
import com.viictrp.financeapp.ui.screens.MainScreen
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModel
import com.viictrp.financeapp.ui.screens.auth.viewmodel.AuthViewModelFactory
import com.viictrp.financeapp.ui.screens.main.HomeScreen
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.color.background)
        enableEdgeToEdge()
        setContent {
            FinanceAppTheme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthManager(LocalContext.current))
    )

    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    val navController = rememberNavController()
    FinanceAppTheme {
        HomeScreen(navController, balanceViewModel, authViewModel)
    }
}