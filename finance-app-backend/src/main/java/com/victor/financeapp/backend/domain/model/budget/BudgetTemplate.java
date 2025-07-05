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
public class BudgetTemplate {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private List<BudgetTemplateCategory> categories;

    public Budget createBudgetForMonth(YearMonth month) {
        var budgetCategories = this.categories.stream()
                .map(templateCategory -> BudgetCategory.builder()
                        .name(templateCategory.getName())
                        .budgetedAmount(templateCategory.getBudgetedAmount())
                        .build())
                .toList();

        return Budget.builder()
                .userId(this.userId)
                .month(month)
                .totalAmount(this.totalAmount)
                .categories(budgetCategories)
                .build();
    }
}
