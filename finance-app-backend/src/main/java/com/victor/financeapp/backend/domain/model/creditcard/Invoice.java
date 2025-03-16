package com.victor.financeapp.backend.domain.model.creditcard;

import com.victor.financeapp.backend.domain.model.Transaction;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Invoice {

    private final Boolean isClosed;
    private final YearMonth yearMonth;
    private final CreditCard creditCard;
    private final List<Transaction> transactions;

    private Invoice(CreditCard creditCard, YearMonth yearMonth) {
        Assert.notNull(yearMonth, "Invoice's month cannot be null");
        isClosed = false;
        this.creditCard = creditCard;
        this.yearMonth = yearMonth;
        transactions = new ArrayList<>();
    }

    static Invoice create(CreditCard creditCard, YearMonth yearMonth) {
        return new Invoice(creditCard, yearMonth);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setInvoice(this);
        this.transactions.add(transaction);
    }
}
