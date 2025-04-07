package com.victor.financeapp.backend.domain.model.user;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.victor.financeapp.backend.domain.model.common.Transaction.TransactionType.RECURRING;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final String lastname;
    private final BigDecimal salary;
    private final boolean active;
    private final List<CreditCard> creditCards;
    private final List<Transaction> transactions;
    private final List<Transaction> recurringExpenses;
    private final List<MonthClosure> monthClosures;
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
        creditCards = new ArrayList<>();
        transactions = new ArrayList<>();
        monthClosures = new ArrayList<>();
        this.recurringExpenses = new ArrayList<>();
        this.properties = new HashMap<>();
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

    public Balance calculateBalance(BigDecimal dollar) {
        var grossSalary = this.salary.multiply(dollar);
        var currencyConversionTax = new BigDecimal(getProperty("CURRENCY_CONVERSION_TAX")
                .orElse("0.0"));

        var salaryTax = new BigDecimal(getProperty("SALARY_TAX")
                .orElse("0.0"));
        var salaryMinusConversionTax = grossSalary.subtract(grossSalary.multiply(currencyConversionTax));
        var salaryMinusTax = salaryMinusConversionTax.subtract(salaryMinusConversionTax.multiply(salaryTax));

        var expenses = calculateExpenses();

        return Balance.builder()
                .nonConvertedSalary(grossSalary)
                .exchangeTaxValue(currencyConversionTax)
                .taxValue(salaryTax)
                .salary(salaryMinusTax)
                .transactions(transactions)
                .recurringExpenses(recurringExpenses)
                .creditCards(creditCards)
                .expenses(expenses)
                .available(salaryMinusTax.subtract(expenses))
                .build();
    }

    private BigDecimal calculateExpenses() {
        var transactionsAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal creditCardsTotal = creditCards.parallelStream()
                .map(CreditCard::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return transactionsAmount.add(creditCardsTotal);
    }

    public void populateRecurringExpenses() {
        this.recurringExpenses.addAll(
                this.transactions
                        .stream().filter(transaction -> RECURRING.equals(transaction.getType()))
                        .toList()
        );
    }

    public Optional<String> getProperty(String propertyName) {
        if (this.properties.containsKey(propertyName)) {
            return Optional.of(this.properties.get(propertyName));
        }

        return Optional.empty();
    }

    public void addProperty(String name, String value) {
        this.properties.put(name, value);
    }

    public void addRecurringExpenses(List<Transaction> recurringExpenses) {
        this.recurringExpenses.addAll(recurringExpenses);
    }
}
