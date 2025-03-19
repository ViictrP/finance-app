package com.victor.financeapp.backend.domain.model.common;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import com.victor.financeapp.backend.domain.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class Transaction {
    private final Long id;
    private String description;
    private String category;
    private BigDecimal amount;
    private Boolean isInstallment = false;
    private Integer installmentNumber;
    private String installmentId;
    private Integer installmentAmount;
    private LocalDateTime date;
    private TransactionType type;

    @Setter
    private Invoice invoice;

    @Setter
    private User user;

    @Getter
    public enum TransactionType {
        RECURRING("RECURRING"),
        DEFAULT("DEFAULT");

        private final String name;

        TransactionType(String name) {
            this.name = name;
        }
    }
}
