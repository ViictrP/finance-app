package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("credit_card")
@Getter
@Setter
class CreditCardEntity extends Entity<Long> {
    private String title;
    private String description;
    private String number;
    private String color;
    private Integer invoiceClosingDay;
    private Long userId;
}
