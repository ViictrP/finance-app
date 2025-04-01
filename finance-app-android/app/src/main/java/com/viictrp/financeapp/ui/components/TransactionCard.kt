package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionCard(title: String, category: String, amount: String, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.ShoppingCart,
                    modifier = Modifier.size(26.dp),
                    contentDescription = "Select Month",
                    tint = Color.Black,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        category,
                        style = LocalTextStyle.current.copy(fontSize = 16.sp),
                        color = Color.Gray
                    )
                    Text(
                        title,
                        style = LocalTextStyle.current.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    date,
                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                    color = Color.Gray
                )
                Text(
                    amount,
                    style = LocalTextStyle.current.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}