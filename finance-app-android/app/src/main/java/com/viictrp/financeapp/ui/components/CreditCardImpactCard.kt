package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.viictrp.financeapp.ui.theme.PrimaryDark
import com.viictrp.financeapp.ui.theme.SecondaryDark

@Composable
fun CreditCardImpactCard(name: String, percentage: String, amount: String, color: Color) {
    Card(
        modifier = Modifier
            .width(190.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    name,
                    style = LocalTextStyle.current.copy(fontSize = 20.sp),
                    fontWeight = FontWeight.Bold,
                    color = SecondaryDark
                )
                Text(
                    percentage,
                    style = LocalTextStyle.current.copy(fontSize = 18.sp),
                    color = SecondaryDark
                )
            }
            Spacer(Modifier.size(4.dp))
            Text(
                amount,
                style = LocalTextStyle.current.copy(fontSize = 18.sp),
                color = SecondaryDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardImpactPreview() {
    FinanceAppTheme {
        CreditCardImpactCard(
            name = "Card",
            percentage = "99.9%",
            amount = "R$ 0,00",
            color = PrimaryDark
        )
    }
}