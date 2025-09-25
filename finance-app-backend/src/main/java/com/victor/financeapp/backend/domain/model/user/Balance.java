package com.victor.financeapp.backend.domain.model.user;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Balance {

    private List<Transaction> transactions;
    private List<CreditCard> creditCards;
    private List<MonthClosure> monthClosures;
    private List<Transaction> lastAddedTransactions;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal available;
    private BigDecimal taxValue;
    private BigDecimal exchangeTaxValue;
    private BigDecimal nonConvertedSalary;
    private Map<Long, BigDecimal> creditCardExpense;
    private @Setter YearMonth yearMonth;
    private @Setter User user;
    private BigDecimal dollar;

    public void calculateBalance(BigDecimal userSalary, BigDecimal dollar, BigDecimal currencyConversionTax, BigDecimal salaryTax, BigDecimal socialSecurity) {
        var grossSalary = userSalary.multiply(dollar);
        var salaryMinusConversionTax = grossSalary.subtract(grossSalary.multiply(currencyConversionTax));
        var salaryMinusTax = salaryMinusConversionTax.subtract(salaryMinusConversionTax.multiply(salaryTax));
        var salaryMinusSocialSecurity = salaryMinusTax.subtract(socialSecurity);

        this.dollar = dollar;
        this.nonConvertedSalary = grossSalary;
        this.exchangeTaxValue = currencyConversionTax;
        this.taxValue = salaryTax;
        this.salary = salaryMinusSocialSecurity;
        this.expenses = calculateExpenses();
        this.available = salaryMinusSocialSecurity.subtract(expenses);
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

    public void addCreditCard(CreditCard newCreditCard) {
        this.creditCards.add(newCreditCard);
    }

    public MonthClosure getMonthClosure() {
        return MonthClosure.builder()
                .month(yearMonth.getMonth().name().substring(0, 3))
                .year(yearMonth.getYear())
                .available(available)
                .expenses(expenses)
                .index(yearMonth.getMonthValue())
                .total(salary)
                .finalUsdToBRL(dollar)
                .user(user)
                .build();
    }
}
