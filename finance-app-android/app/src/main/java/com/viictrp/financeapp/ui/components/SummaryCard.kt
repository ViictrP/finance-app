package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SummaryCard(salary: BigDecimal, expenses: BigDecimal, available: BigDecimal) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InternalSummaryCard(
                "Salário",
                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(salary),
                MaterialTheme.colorScheme.tertiary
            )
            InternalSummaryCard(
                "Gastos",
                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(expenses),
                MaterialTheme.colorScheme.tertiary
            )
            InternalSummaryCard(
                "Livre",
                NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(available),
                MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun InternalSummaryCard(title: String, amount: String, color: Color) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                style = LocalTextStyle.current.copy(fontSize = 16.sp),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            amount,
            color = MaterialTheme.colorScheme.secondary,
            style = LocalTextStyle.current.copy(fontSize = 13.sp),
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SummaryCardPreview() {
    FinanceAppTheme {
        SummaryCard(BigDecimal.valueOf(27000), BigDecimal.valueOf(8500), BigDecimal.valueOf(19500))
    }
}