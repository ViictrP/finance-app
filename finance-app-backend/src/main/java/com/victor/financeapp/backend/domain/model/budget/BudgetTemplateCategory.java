package com.victor.financeapp.backend.domain.model.budget;

import com.victor.financeapp.backend.domain.model.common.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BudgetTemplateCategory {
    private Long id;
    private Category name;
    private BigDecimal budgetedAmount;
}
