package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;

public record BudgetTemplateCategoryDTO(
        Long id,
        String name,
        BigDecimal budgetedAmount
) {
}
