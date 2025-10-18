package com.viictrp.financeapp.ui.screens.secure

import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.data.remote.dto.MonthClosureDTO
import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.model.transaction.TransactionType
import com.viictrp.financeapp.ui.screens.secure.viewmodel.BalanceState
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

object PreviewData {
    val sampleTransactions = listOf(
        TransactionDTO(
            id = 1,
            description = "Netflix",
            amount = BigDecimal("39.90"),
            category = "Assinaturas",
            type = TransactionType.RECURRING,
            date = LocalDateTime.now().minusDays(2),
            creditCardId = 1
        ),
        TransactionDTO(
            id = 2,
            description = "Almoço",
            amount = BigDecimal("45.50"),
            category = "Alimentação",
            type = TransactionType.DEFAULT,
            date = LocalDateTime.now().minusDays(1),
            creditCardId = 1
        ),
        TransactionDTO(
            id = 3,
            description = "Gasolina",
            amount = BigDecimal("150.00"),
            category = "Transporte",
            type = TransactionType.DEFAULT,
            date = LocalDateTime.now(),
            creditCardId = 2
        )
    )

    val sampleCreditCards = listOf(
        CreditCardDTO(
            id = 1,
            title = "Cartão Principal",
            description = "Mastercard Platinum",
            color = "BLUE",
            number = "**** 1234",
            invoiceClosingDay = 28,
            totalInvoiceAmount = BigDecimal("1500.75")
        ),
        CreditCardDTO(
            id = 2,
            title = "Cartão Secundário",
            description = "Visa Gold",
            color = "GOLD",
            number = "**** 5678",
            invoiceClosingDay = 15,
            totalInvoiceAmount = BigDecimal("850.20")
        )
    )

    val sampleMonthClosures = listOf(
        MonthClosureDTO(
            month = "JAN",
            year = YearMonth.now().year,
            total = BigDecimal("5000"),
            available = BigDecimal("2000"),
            expenses = BigDecimal("3000"),
            index = 0,
            finalUsdToBRL = BigDecimal("5.00")
        )
    )

    val sampleBalance = BalanceDTO(
        transactions = sampleTransactions,
        lastAddedTransactions = sampleTransactions.take(2),
        creditCards = sampleCreditCards,
        monthClosures = sampleMonthClosures,
        salary = BigDecimal("5000"),
        expenses = BigDecimal("2350.45"),
        available = BigDecimal("2649.55")
    )

    val loadingState = BalanceState(loading = true)

    val loadedState = BalanceState(
        loading = false,
        balance = sampleBalance,
        currentBalance = sampleBalance,
        selectedYearMonth = YearMonth.now(),
        creditCards = sampleCreditCards,
        isInitialized = true
    )
}
