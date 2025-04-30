package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface TransactionEntityRepository extends ReactiveCrudRepository<TransactionEntity, Long> {

    @Query(
            """
                SELECT *
                FROM finance_app.transaction
                WHERE user_id = :userId
                  AND invoice_id IS NULL
                  AND deleted IS FALSE
                  AND delete_date IS NULL
                  AND (
                      type = 'RECURRING'
                      OR (
                          type = 'DEFAULT'
                          AND EXTRACT(MONTH FROM date) = :month
                          AND EXTRACT(YEAR FROM date) = :year
                      )
                  )
                ORDER BY date DESC;
            """)
    Flux<TransactionEntity> findByUserIdAndInvoiceIdIsNull(Long userId, int month, int year);

    Flux<TransactionEntity> findByInvoiceIdAndDeletedIsFalseOrderByDateDesc(Long invoiceId);

    @Query("""
        SELECT *
        FROM finance_app.transaction
        WHERE user_id = :userId
        ORDER BY transaction.created_at DESC
        LIMIT 5
    """)
    Flux<TransactionEntity> findLastFiveAdded(Long userId);

    Flux<TransactionEntity> findAllByInstallmentId(String installmentId);
}
