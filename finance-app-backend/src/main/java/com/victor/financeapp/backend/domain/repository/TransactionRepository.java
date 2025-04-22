package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface TransactionRepository {
    Flux<Transaction> findUserTransactionsOn(Long id, YearMonth yearMonth);
    Flux<Transaction> findInvoiceTransactionsOn(Long invoiceId);

    Mono<Transaction> save(Transaction transactions);
}
