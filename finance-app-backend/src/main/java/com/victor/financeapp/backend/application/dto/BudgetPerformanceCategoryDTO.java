package com.victor.financeapp.backend.application.dto;

import com.victor.financeapp.backend.domain.model.common.Category;

import java.math.BigDecimal;

public record BudgetPerformanceCategoryDTO(
        Category categoryName,
        BigDecimal budgetedAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount
) {
}
