package com.victor.financeapp.backend.domain.model.common;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Transaction {
    private final Long id;
    private @Setter String description;
    private @Setter String category;
    private @Setter BigDecimal amount;
    private @Setter Boolean isInstallment = false;
    private @Setter Integer installmentNumber;
    private @Setter String installmentId;
    private @Setter Integer installmentAmount;
    private @Setter LocalDateTime date;
    private @Setter TransactionType type;
    private @Setter Invoice invoice;
    private @Setter Long userId;

    public Transaction(Long id, BigDecimal amount, String category, LocalDateTime date, String description, Integer installmentAmount, TransactionType type) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.installmentAmount = installmentAmount;
        this.type = type;
    }

    public List<Transaction> getInstallmentTransactions() {
        if (this.installmentAmount == 1 || type.equals(TransactionType.RECURRING)) {
            this.isInstallment = false;
            return List.of(this);
        }

        var transactions = new ArrayList<Transaction>();
        var uuid = UUID.randomUUID().toString();
        for (int number = 0; number < this.installmentAmount; number++) {
            var transaction = new Transaction(
                    null,
                    this.amount.divide(BigDecimal.valueOf(this.installmentAmount), 2, RoundingMode.HALF_UP),
                    this.category,
                    this.date.plusMonths(number),
                    this.description,
                    this.installmentAmount,
                    this.type);

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
