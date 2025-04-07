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
import com.viictrp.financeapp.application.dto.TransactionDTO
import com.viictrp.financeapp.application.enums.TransactionType
import com.viictrp.financeapp.ui.helper.categoryHelper
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionCard(transaction: TransactionDTO) {
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
                        categoryHelper(transaction.category),
                        style = LocalTextStyle.current.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        transaction.description,
                        style = LocalTextStyle.current.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.date.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                    ),
                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5F)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(transaction.amount),
                    style = LocalTextStyle.current.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium
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
            TransactionDTO(
                id = 1,
                description = "Description",
                amount = BigDecimal.valueOf(100.0),
                category = "shop",
                type = TransactionType.RECURRING,
                date = OffsetDateTime.now(),
                isInstallment = false,
                installmentAmount = BigDecimal.valueOf(100.0),
                installmentId = null,
                creditCardId = null,
                installmentNumber = 1
            )
        )
    }
}
