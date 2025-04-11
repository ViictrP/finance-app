package com.viictrp.financeapp.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.viictrp.financeapp.ui.components.CardCarousel
import com.viictrp.financeapp.ui.components.CarouselItem
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModel
import com.viictrp.financeapp.ui.screens.main.viewmodel.BalanceViewModelFactory
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

data class CreditCardCarouselItem(
    private val id: String,
    private val color: String,
    private val title: String,
    private val description: String
) : CarouselItem {
    override fun getKey() = id
    override fun getColor() = color
    override fun getTitle() = title
    override fun getDescription() = description
}

@Composable
fun CreditCardScreen(navController: NavController, balanceViewModel: BalanceViewModel) {
    val balance by balanceViewModel.currentBalance.collectAsState()
    var selectedCard: CreditCardCarouselItem? by remember {
        mutableStateOf(null)
    }

    var carouselItems: List<CreditCardCarouselItem> by remember {
        mutableStateOf(emptyList())
    }

    LaunchedEffect(balance) {
        balance?.let {
            selectedCard = CreditCardCarouselItem(
                id = it.creditCards[0].id.toString(),
                title = it.creditCards[0].title,
                description = it.creditCards[0].description,
                color = it.creditCards[0].color
            )

            carouselItems = it.creditCards
                .map { creditCard ->
                    CreditCardCarouselItem(
                        id = creditCard.id.toString(),
                        title = creditCard.title,
                        description = creditCard.description,
                        color = creditCard.color
                    )
                }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp), Arrangement.SpaceBetween
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
                CardCarousel(carouselItems) { card ->
                    selectedCard = card as CreditCardCarouselItem?
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp), Arrangement.SpaceBetween
                ) {
                    Text(selectedCard?.getTitle() ?: "")
                }
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