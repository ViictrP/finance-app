package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface InvoiceRepository {
    Mono<Invoice> findCreditCardsInvoiceOn(Long creditCardId, YearMonth yearMonth);
    Flux<Invoice> findCreditCardInvoices(Long creditCardId);
    Mono<Invoice> save(Invoice invoice);
}
