package com.victor.financeapp.backend.domain.model;

import org.springframework.util.Assert;

public record CreditCard(
        String title,
        String description,
        String number,
        String color,
        Integer invoiceClosingDay,
        User user
) {

    public CreditCard {
        Assert.hasText(title, "The credit card title is required");
        Assert.hasText(number, "The credit card number is required");
        Assert.notNull(invoiceClosingDay, "The credit card title is required");
        Assert.state(invoiceClosingDay > 0, "The credit card invoice closing day is invalid");
    }
}
