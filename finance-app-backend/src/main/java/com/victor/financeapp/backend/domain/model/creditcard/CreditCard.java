package com.victor.financeapp.backend.domain.model.creditcard;

import com.victor.financeapp.backend.domain.exception.InvoiceNotFoundException;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CreditCard {

    private final Long id;
    private final String title;
    private final String number;
    private final Integer invoiceClosingDay;
    private final Long userId;
    private BigDecimal totalInvoiceAmount = BigDecimal.ZERO;

    private @Setter String description;
    private @Setter String color;
    private @Setter List<Invoice> invoices;

    @Builder
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
        return invoices.stream().filter(invoice -> invoice.getYearMonth().equals(yearMonth))
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
        transaction.setUserId(this.userId);
    }

    public List<Transaction> createInstallmentTransactions(Transaction originalTransaction) {
        originalTransaction.setType(Transaction.TransactionType.DEFAULT);
        return originalTransaction.getInstallmentTransactions();
    }

    public BigDecimal calculateTotal() {
        totalInvoiceAmount = invoices.parallelStream().map(Invoice::calculateTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalInvoiceAmount;
    }

    public Invoice getInvoice(YearMonth yearMonth) {
        return invoices.stream().filter(invoice -> yearMonth.equals(invoice.getYearMonth())).findFirst().orElseThrow(() -> new InvoiceNotFoundException(id, yearMonth));
    }

    public static CreditCard createNew(Long userId, String title, String description, String color, String number, Integer invoiceClosingDay) {
        var newCreditCard = CreditCard.builder().title(title).number(number).userId(userId).invoiceClosingDay(invoiceClosingDay).build();

        newCreditCard.setDescription(description);
        newCreditCard.setColor(color);

        return newCreditCard;
    }
}
