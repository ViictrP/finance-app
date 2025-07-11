package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("budget_template_category")
@Getter
@Setter
class BudgetTemplateCategoryEntity extends Entity<Long> {
    private Long templateId;
    private String category;
    private BigDecimal budgetedAmount;
}
