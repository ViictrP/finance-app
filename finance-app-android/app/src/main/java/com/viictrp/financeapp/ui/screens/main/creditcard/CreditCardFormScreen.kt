package com.viictrp.financeapp.ui.screens.main.creditcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@Composable
fun CreditCardFormScreen(navController: NavController, balanceModel: BalanceViewModel) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Adicionar um Cartão de Crédito",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 24.sp)
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        " Para adicionar um cartão de crédito, preencha todas as informações obrigatórias marcadas com * (asterísco). ",
                        style = LocalTextStyle.current.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardFormScreenPreview() {
    val navController = rememberNavController()
    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    FinanceAppTheme {
        CreditCardFormScreen(navController, balanceViewModel)
    }
}