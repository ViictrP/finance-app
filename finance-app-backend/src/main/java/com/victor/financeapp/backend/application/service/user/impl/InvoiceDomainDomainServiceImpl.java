package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.user.InvoiceDomainService;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceDomainDomainServiceImpl implements InvoiceDomainService {

    private final InvoiceRepository invoiceRepository;
    private final TransactionRepository transactionRepository;

    public Mono<CreditCard> populateCreditCardInvoice(CreditCard creditCard, YearMonth yearMonth) {
        return invoiceRepository.findCreditCardsInvoiceOn(creditCard.getId(), yearMonth)
                .flatMap(this::populateInvoiceTransactions)
                .flatMap(invoice -> {
                    creditCard.addInvoice(invoice);
                    return Mono.just(creditCard);
                });
    }

    private Mono<Invoice> populateInvoiceTransactions(Invoice invoice) {
        return transactionRepository.findInvoiceTransactionsOn(invoice.getId())
                .collectList()
                .doOnNext(invoice::addTransactions)
                .thenReturn(invoice);
    }
}
