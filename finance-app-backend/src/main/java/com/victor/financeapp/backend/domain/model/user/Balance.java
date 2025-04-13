package com.victor.financeapp.backend.domain.model.user;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Balance {

    private List<Transaction> transactions;
    private List<Transaction> recurringExpenses;
    private List<CreditCard> creditCards;
    private List<MonthClosure> monthClosures;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal available;
    private BigDecimal taxValue;
    private BigDecimal exchangeTaxValue;
    private BigDecimal nonConvertedSalary;
    private Map<Long, BigDecimal> creditCardExpense;

    public void calculateBalance(BigDecimal userSalary, BigDecimal dollar, BigDecimal currencyConversionTax, BigDecimal salaryTax) {
        var grossSalary = userSalary.multiply(dollar);
        var salaryMinusConversionTax = grossSalary.subtract(grossSalary.multiply(currencyConversionTax));
        var salaryMinusTax = salaryMinusConversionTax.subtract(salaryMinusConversionTax.multiply(salaryTax));

        this.nonConvertedSalary = grossSalary;
        this.exchangeTaxValue = currencyConversionTax;
        this.taxValue = salaryTax;
        this.salary = salaryMinusTax;
        this.expenses = calculateExpenses();
        this.available = salaryMinusTax.subtract(expenses);
    }

    private BigDecimal calculateExpenses() {
        var transactionsAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        var recurringExpensesAmount = recurringExpenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal creditCardsTotal = creditCards.parallelStream()
                .map(CreditCard::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return transactionsAmount.add(creditCardsTotal).add(recurringExpensesAmount);
    }
}
