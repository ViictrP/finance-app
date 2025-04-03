package com.viictrp.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.screens.HomeScreen
import com.viictrp.financeapp.ui.screens.MainScreen
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import com.viictrp.financeapp.ui.viewmodel.BalanceViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    val navController = rememberNavController()
    FinanceAppTheme {
        HomeScreen(navController, BalanceViewModel(ApiService()))
    }
}