package com.victor.financeapp.backend.domain.model;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User {

    private String name;
    private String lastName;
    private BigDecimal salary;
    private boolean active;
    private List<CreditCard> creditCards;
    private List<Transaction> transactions;
    private List<MonthClosure> monthClosures;

    public User() {
        Assert.hasText(name, "The user name is required");
        Assert.hasText(lastName, "The user lastname is required");
        Assert.notNull(salary, "The user salary is required");
        creditCards = new ArrayList<>();
        transactions = new ArrayList<>();
        monthClosures = new ArrayList<>();
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }
}
