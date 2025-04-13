package com.viictrp.financeapp.ui.screens.main.creditcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.viictrp.financeapp.application.service.ApiService
import com.viictrp.financeapp.ui.components.CardCarousel
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.components.TransactionCard
import com.viictrp.financeapp.ui.components.extension.toFormattedYearMonth
import com.viictrp.financeapp.ui.components.extension.toLong
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.YearMonth
import java.util.Locale

data class CreditCardCarouselItem(
    private val id: String,
    private val color: String,
    private val title: String,
    private val description: String,
    private val detail: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
    override fun getDetail(): String = detail
}

@Composable
fun CreditCardScreen(navController: NavController, balanceViewModel: BalanceViewModel) {
    val balance by balanceViewModel.currentBalance.collectAsState()

    var selectedCardId by remember {
        mutableStateOf(balance?.creditCards?.firstOrNull()?.id?.toString())
    }

    val selectedCreditCard = remember(selectedCardId, balance) {
        balance?.creditCards?.find { it.id.toString() == selectedCardId }
    }

    val transactions = remember(selectedCreditCard) {
        selectedCreditCard?.invoices?.firstOrNull()?.transactions ?: emptyList()
    }

    var carouselItems: List<CreditCardCarouselItem> = remember(balance) {
        balance?.creditCards
            ?.map { creditCard ->
                CreditCardCarouselItem(
                    id = creditCard.id.toString(),
                    title = creditCard.title,
                    description = creditCard.number,
                    color = creditCard.color,
                    detail = creditCard.invoiceClosingDay.toString()
                )
            } ?: emptyList()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = padding
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row {
                        Text(
                            "Seus Cartões",
                            fontWeight = FontWeight.Bold,
                            style = LocalTextStyle.current.copy(fontSize = 24.sp)
                        )
                        Text(
                            " (${carouselItems.size})",
                            fontWeight = FontWeight.Normal,
                            style = LocalTextStyle.current.copy(fontSize = 24.sp)
                        )
                    }
                }
            }

            item {
                CardCarousel(
                    carouselItems,
                    onPageChanged = { card -> selectedCardId = card.getKey() },
                    cardHeight = 180.dp
                )
            }

            item {
                Spacer(modifier = Modifier.size(48.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = selectedCreditCard?.title ?: "",
                            fontWeight = FontWeight.Bold,
                            style = LocalTextStyle.current.copy(fontSize = 20.sp)
                        )
                        Text(
                            "Lançamentos da fatura de ${
                                YearMonth.now().toFormattedYearMonth("MMMM")
                            }",
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { navController.navigate("invoice/$selectedCardId") }) {
                            Text(
                                "Ver faturas",
                                style = LocalTextStyle.current.copy(fontSize = 18.sp),
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                CustomIcons.DuoTone.Calendar,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Select Month",
                                tint = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                        Text(
                            text = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                                .format(selectedCreditCard?.totalInvoiceAmount ?: BigDecimal(0.0)),
                            fontWeight = FontWeight.Normal,
                            style = LocalTextStyle.current.copy(fontSize = 20.sp)
                        )
                    }
                }
            }

            items(transactions.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .animateItem()
                ) {
                    TransactionCard(transactions[index])
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardScreenPreview() {
    val navController = rememberNavController()
    val balanceViewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(ApiService())
    )

    FinanceAppTheme {
        CreditCardScreen(navController, balanceViewModel)
    }
}