package com.victor.financeapp.backend.application.dto;

import com.victor.financeapp.backend.domain.model.common.Category;

import java.math.BigDecimal;

public record BudgetCategoryDTO(
    Long id,
    Category name,
    BigDecimal budgetedAmount
) {}
