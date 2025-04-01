package com.viictrp.financeapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.viictrp.financeapp.ui.components.Header

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header("Victor Prado")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f)),
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "gastos do mês",
                                style = LocalTextStyle.current.copy(fontSize = 14.sp),
                                color = Color.Gray
                            )
                            Text(
                                "Mar",
                                style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                color = Color.Blue,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { navController.navigate("balance") }
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            "R$ 23.143,66",
                            style = LocalTextStyle.current.copy(fontSize = 40.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}