package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record BudgetPerformanceDTO(
        YearMonth month,
        BigDecimal totalBudgeted,
        BigDecimal totalSpent,
        BigDecimal totalRemaining,
        List<BudgetPerformanceCategoryDTO> categories
) {
}
