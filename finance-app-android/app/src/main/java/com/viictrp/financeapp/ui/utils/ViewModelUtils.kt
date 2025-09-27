package com.viictrp.financeapp.ui.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.viictrp.financeapp.auth.AuthViewModel
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceViewModel

@Composable
fun rememberBalanceViewModel(): BalanceViewModel {
    val context = LocalContext.current
    return hiltViewModel<BalanceViewModel>(context as ComponentActivity)
}

@Composable
fun rememberAuthViewModel(): AuthViewModel {
    val context = LocalContext.current
    return hiltViewModel<AuthViewModel>(context as ComponentActivity)
}
