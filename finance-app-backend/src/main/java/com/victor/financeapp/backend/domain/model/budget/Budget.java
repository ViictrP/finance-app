package com.victor.financeapp.backend.domain.model.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Budget {
    private Long id;
    private Long userId;
    private YearMonth month;
    private BigDecimal totalAmount;
    private List<BudgetCategory> categories;
}