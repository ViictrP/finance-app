package com.victor.financeapp.backend.domain.model.common;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Transaction {
    private final Long id;
    private @Setter String description;
    private Category category;
    private @Setter BigDecimal amount;
    private @Setter Boolean isInstallment = false;
    private @Setter Integer installmentNumber;
    private @Setter String installmentId;
    private @Setter Integer installmentAmount;
    private @Setter LocalDateTime date;
    private @Setter TransactionType type;
    private @Setter Invoice invoice;
    private @Setter Long userId;

    public List<Transaction> getInstallmentTransactions() {
        if (this.installmentAmount == 1 || type.equals(TransactionType.RECURRING)) {
            this.isInstallment = false;
            return List.of(this);
        }

        var transactions = new ArrayList<Transaction>();
        var uuid = UUID.randomUUID().toString();
        for (int number = 0; number < this.installmentAmount; number++) {
            var transaction = Transaction.builder()
                    .amount(this.amount.divide(BigDecimal.valueOf(this.installmentAmount), 2, RoundingMode.HALF_UP))
                    .category(this.category)
                    .date(this.date.plusMonths(number))
                    .description(this.description)
                    .installmentAmount(this.installmentAmount)
                    .type(this.type)
                    .build();

            transaction.installmentNumber = number + 1;
            transaction.isInstallment = true;
            transaction.installmentId = uuid;
            transactions.add(transaction);
        }
        return transactions;
    }

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
