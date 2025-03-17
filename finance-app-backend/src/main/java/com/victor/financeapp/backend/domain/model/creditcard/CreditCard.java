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

    private final Long id;
    private final String title;
    @Setter
    private String description;
    private final String number;
    @Setter
    private String color;
    private final Integer invoiceClosingDay;
    private final List<Invoice> invoices;
    private final Long userId;

    public CreditCard(Long id, String title, String number, Integer invoiceClosingDay, Long userId) {
        Assert.hasText(title, "The credit card title is required");
        Assert.hasText(number, "The credit card number is required");
        Assert.notNull(invoiceClosingDay, "The credit card title is required");
        Assert.state(invoiceClosingDay > 0, "The credit card invoice closing day is invalid");

        this.invoices = new ArrayList<>();
        this.title = title;
        this.number = number;
        this.invoiceClosingDay = invoiceClosingDay;
        this.id = id;
        this.userId = userId;
    }

    private Invoice addInvoice(YearMonth yearMonth) {
        var newInvoice = Invoice.create(this.id, yearMonth);
        this.invoices.add(newInvoice);
        return newInvoice;
    }

    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
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
