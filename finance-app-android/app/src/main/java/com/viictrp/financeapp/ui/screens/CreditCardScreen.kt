package com.viictrp.financeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.Header

@Composable
fun CreditCardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header("Victor Prado")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(top = 24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), Arrangement.SpaceBetween
                ) {
                    Text(
                        "Seus Cartões",
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(fontSize = 24.sp)
                    )
                }
            }
        }
    }
}