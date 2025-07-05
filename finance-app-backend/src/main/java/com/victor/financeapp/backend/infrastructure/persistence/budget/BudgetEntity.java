package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("budget")
@Getter
@Setter
class BudgetEntity extends Entity<Long> {
    private Long userId;
    private String month;
    private BigDecimal totalAmount;
}