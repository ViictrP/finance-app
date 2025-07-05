package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record BudgetDTO(
    Long id,
    YearMonth month,
    BigDecimal totalAmount,
    List<BudgetCategoryDTO> categories
) {}