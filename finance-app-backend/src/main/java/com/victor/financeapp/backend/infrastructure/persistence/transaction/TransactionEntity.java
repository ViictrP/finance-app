package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("transaction")
@Getter
@Setter
class TransactionEntity extends Entity<Long> {
    private String description;
    private String category;
    private BigDecimal amount;
    private Boolean isInstallment = false;
    private Integer installmentNumber;
    private String installmentId;
    private Integer installmentAmount;
    private LocalDateTime date;
    private String type;
    private Long userId;
    private Long invoiceId;
}
