package com.victor.financeapp.backend.domain.model.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BudgetTemplateCategory {
    private Long id;
    private String name;
    private BigDecimal budgetedAmount;
}
