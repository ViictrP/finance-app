package com.viictrp.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.viictrp.financeapp.ui.screens.MainScreen
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { false }
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