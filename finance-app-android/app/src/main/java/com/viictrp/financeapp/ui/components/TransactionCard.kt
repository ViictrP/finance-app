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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@Composable
fun TransactionCard(title: String, category: String, amount: String, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
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
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        category,
                        style = LocalTextStyle.current.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        title,
                        style = LocalTextStyle.current.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    date,
                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    amount,
                    style = LocalTextStyle.current.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionCardPreview() {
    FinanceAppTheme {
        TransactionCard(
            title = "Title",
            category = "Category",
            amount = "R$0,00",
            date = "22/03/2023"
        )
    }
}
