package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("budget_category")
@Getter
@Setter
class BudgetCategoryEntity extends Entity<Long> {
    private Long budgetId;
    private String name;
    private BigDecimal budgetedAmount;
}