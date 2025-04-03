package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("month_closure")
@Getter
@Setter
class MonthClosureEntity extends Entity<Long> {
    private String month;
    private Integer year;
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal expenses;
    private Integer index;
    private BigDecimal finalUsdToBRL;
    private Long userId;
}
