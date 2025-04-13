package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.YearMonth;

@Repository
@RequiredArgsConstructor
class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionEntityRepository repository;
    private final TransactionEntityMapper mapper;

    @Override
    public Flux<Transaction> findUserTransactionsOn(Long id, YearMonth yearMonth) {
        var month = yearMonth.getMonthValue();
        var year = yearMonth.getYear();
        return repository.findByUserIdAndInvoiceIdIsNull(id, month, year)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Transaction> findAllRecurringExpenses(Long userId) {
        return repository.findAllRecurringExpenses(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Transaction> findInvoiceTransactionsOn(Long invoiceId) {
        return repository.findByInvoiceIdAndDeletedIsFalseOrderByDateDesc(invoiceId)
                .map(mapper::toDomain);
    }
}
