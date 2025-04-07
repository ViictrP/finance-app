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
                        AND EXTRACT(MONTH FROM date) = :month
                        AND EXTRACT(YEAR FROM date) = :year
                        AND deleted IS FALSE
                        AND delete_date IS NULL
                    """)
    Flux<TransactionEntity> findByUserIdAndInvoiceIdIsNull(Long userId, int month, int year);

    Flux<TransactionEntity> findByInvoiceIdAndDeletedIsFalse(Long invoiceId);

    @Query(
            """
                    SELECT id,
                           description,
                           category,
                           amount,
                           false as is_installment,
                           0 as installment_number,
                           null as installment_id,
                           amount as installment_amount,
                           created_at as date,
                           'RECURRING' as type,
                           user_id,
                           null as invoice_id
                    FROM finance_app.recurring_expense
                    WHERE user_id = :userId
                    AND delete_date IS NULL
                    """)
    Flux<TransactionEntity> findAllRecurringExpenses(Long userId);
}
