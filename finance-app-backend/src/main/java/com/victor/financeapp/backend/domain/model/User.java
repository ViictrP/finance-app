package com.victor.financeapp.backend.domain.model;

import lombok.Builder;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record User(
        String name,
        String lastName,
        BigDecimal salary,
        boolean active,
        List<CreditCard> creditCards,
        List<Transaction> transactions,
        List<MonthClosure> monthClosures
) {

    public User {
        Assert.hasText(name, "The user name is required");
        Assert.hasText(lastName, "The user lastname is required");
        Assert.notNull(salary, "The user salary is required");
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCards.add(creditCard);
    }
}
