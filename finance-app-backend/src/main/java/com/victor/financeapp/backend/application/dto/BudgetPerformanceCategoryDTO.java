package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;

public record BudgetPerformanceCategoryDTO(
        String categoryName,
        BigDecimal budgetedAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount
) {
}
