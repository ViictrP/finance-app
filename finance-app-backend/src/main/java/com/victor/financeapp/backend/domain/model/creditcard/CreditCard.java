package com.victor.financeapp.backend.domain.model.creditcard;

import com.victor.financeapp.backend.domain.model.Transaction;
import com.victor.financeapp.backend.domain.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CreditCard {

    private String title;
    private String description;
    private String number;
    private String color;
    private Integer invoiceClosingDay;
    private final List<Invoice> invoices;
    @Setter
    private User user;

    public CreditCard(String title, String number, Integer invoiceClosingDay) {
        Assert.hasText(title, "The credit card title is required");
        Assert.hasText(number, "The credit card number is required");
        Assert.notNull(invoiceClosingDay, "The credit card title is required");
        Assert.state(invoiceClosingDay > 0, "The credit card invoice closing day is invalid");
        invoices = new ArrayList<>();
    }

    private Invoice addInvoice(YearMonth yearMonth) {
        var newInvoice = Invoice.create(this, yearMonth);
        this.invoices.add(newInvoice);
        return newInvoice;
    }

    private Invoice getOrCreateInvoice(YearMonth yearMonth) {
        return invoices.stream()
                .filter(invoice -> invoice.getYearMonth().equals(yearMonth))
                .findFirst()
                .orElse(addInvoice(yearMonth));
    }

    public void addTransaction(Transaction transaction) {
        var transactionDate = YearMonth.from(transaction.getDate());

        if (transaction.getDate().getDayOfMonth() > invoiceClosingDay) {
            transactionDate = transactionDate.plusMonths(1);
        }

        var invoice = getOrCreateInvoice(transactionDate);
        invoice.addTransaction(transaction);
    }
}
