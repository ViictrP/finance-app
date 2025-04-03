package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("invoice")
@Getter
@Setter
class InvoiceEntity extends Entity<Long> {
    private Integer year;
    private String month;
    private boolean isClosed;
    private Long creditCardId;
}
