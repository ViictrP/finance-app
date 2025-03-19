package com.victor.financeapp.backend.domain.model.creditcard;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import lombok.Getter;
import org.springframework.util.Assert;

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

    private Invoice(Long id, Long creditCardId, YearMonth yearMonth) {
        Assert.notNull(yearMonth, "Invoice's month cannot be null");
        Assert.notNull(creditCardId, "Invoice's credit card ID cannot be null");
        isClosed = false;
        this.creditCardId = creditCardId;
        this.yearMonth = yearMonth;
        transactions = new ArrayList<>();
        this.id = id;
    }

    public static Invoice create(Long creditCardId, YearMonth yearMonth) {
        return new Invoice(null, creditCardId, yearMonth);
    }

    public static Invoice create(Long id, Long creditCardId, YearMonth yearMonth) {
        return new Invoice(id, creditCardId, yearMonth);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setInvoice(this);
        this.transactions.add(transaction);
    }

    public void addTransactions(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
    }
}
