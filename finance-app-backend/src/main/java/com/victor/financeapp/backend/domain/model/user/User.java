package com.victor.financeapp.backend.domain.model.user;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final String lastname;
    private final BigDecimal salary;
    private final boolean active;
    @Setter
    private Balance balance;
    private final Map<String, String> properties;

    public User(Long id, String name, String lastname, BigDecimal salary, boolean active) {
        Assert.hasText(name, "The user name is required");
        Assert.hasText(lastname, "The user lastname is required");
        Assert.notNull(salary, "The user salary is required");

        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.salary = salary;
        this.active = active;
        this.properties = new HashMap<>();
    }

    public Optional<String> getProperty(String propertyName) {
        if (this.properties.containsKey(propertyName)) {
            return Optional.of(this.properties.get(propertyName));
        }

        return Optional.empty();
    }

    public void calculateBalance(BigDecimal dollar) {
        var currencyConversionTax = new BigDecimal(getProperty("CURRENCY_CONVERSION_TAX")
                .orElse("0.0"));

        var salaryTax = new BigDecimal(getProperty("SALARY_TAX")
                .orElse("0.0"));

        this.balance.calculateBalance(this.salary, dollar, currencyConversionTax, salaryTax);
    }

    public void addProperty(String name, String value) {
        this.properties.put(name, value);
    }

    public CreditCard addNewCreditCard(String title, String description, String color, String number, Integer invoiceClosingDay) {
        var newCreditCard = CreditCard.createNew(this.id, title, description, color, number, invoiceClosingDay);

        if (balance != null) {
            this.balance.addCreditCard(newCreditCard);
        }

        return newCreditCard;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setUserId(this.id);
    }
}
