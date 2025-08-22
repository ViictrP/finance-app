package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface TransactionRepository {
    Flux<Transaction> findUserTransactionsOn(Long id, YearMonth yearMonth);
    Flux<Transaction> findInvoiceTransactionsOn(Long invoiceId);
    Flux<Transaction> findLastFiveAdded(Long userId);
    Flux<Transaction> findInstallments(String installmentId);
    Mono<Transaction> findById(Long id);

    Mono<Transaction> save(Transaction transactions);

    Mono<Boolean> deleteByIdAndUserId(Long id, Long userId);
    Mono<Boolean> deleteByInstallmentIdAndUserId(String installmentId, Long userId);
}
