package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;

public record BudgetCategoryDTO(
    Long id,
    String name,
    BigDecimal budgetedAmount
) {}