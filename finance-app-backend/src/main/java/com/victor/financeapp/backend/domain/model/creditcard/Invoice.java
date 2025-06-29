package com.victor.financeapp.backend.domain.model.creditcard;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Invoice {

    private final Long id;
    private final Boolean isClosed;
    private final YearMonth yearMonth;
    private final Long creditCardId;
    private final List<Transaction> transactions;

    @Builder
    public Invoice(Long id, Long creditCardId, YearMonth yearMonth) {
        Assert.notNull(yearMonth, "Invoice's month cannot be null");
        Assert.notNull(creditCardId, "Invoice's credit card ID cannot be null");
        isClosed = false;
        this.creditCardId = creditCardId;
        this.yearMonth = yearMonth;
        transactions = new ArrayList<>();
        this.id = id;
    }

    public boolean isNew() {
        return id == null;
    }

    public static Invoice create(Long creditCardId, YearMonth yearMonth) {
        return new Invoice(null, creditCardId, yearMonth);
    }

    public static Invoice create(Long id, Long creditCardId, YearMonth yearMonth) {
        return new Invoice(id, creditCardId, yearMonth);
    }

    public Boolean isClosed() {
        var now = YearMonth.now();
        return now.isAfter(this.yearMonth);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setInvoice(this);
        this.transactions.add(transaction);
    }

    public BigDecimal calculateTotal() {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addTransactions(List<Transaction> transactions) {
        this.transactions.addAll(transactions.stream().peek(tr -> tr.setInvoice(this)).toList());
    }
}
