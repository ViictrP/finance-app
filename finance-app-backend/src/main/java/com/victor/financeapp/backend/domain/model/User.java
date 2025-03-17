package com.victor.financeapp.backend.domain.model;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final String lastname;
    private final BigDecimal salary;
    private final boolean active;
    private final List<CreditCard> creditCards;
    private final List<Transaction> transactions;
    private final List<MonthClosure> monthClosures;

    public User(Long id, String name, String lastname, BigDecimal salary, boolean active) {
        Assert.hasText(name, "The user name is required");
        Assert.hasText(lastname, "The user lastname is required");
        Assert.notNull(salary, "The user salary is required");

        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.salary = salary;
        this.active = active;
        creditCards = new ArrayList<>();
        transactions = new ArrayList<>();
        monthClosures = new ArrayList<>();
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }

    public void addCreditCards(List<CreditCard> creditCards) {
        this.creditCards.addAll(creditCards);
    }

    public void addTransactions(List<Transaction> transactions) {
        this.transactions.addAll(transactions);
    }

    public void addMonthClosures(List<MonthClosure> monthClosures) {
        this.monthClosures.addAll(monthClosures);
    }
}
