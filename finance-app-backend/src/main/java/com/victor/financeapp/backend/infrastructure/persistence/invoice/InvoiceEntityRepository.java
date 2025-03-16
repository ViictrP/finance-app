package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface InvoiceEntityRepository extends ReactiveCrudRepository<InvoiceEntity, Long> {
    Mono<InvoiceEntity> findByCreditCardIdAndMonthAndYear(Long creditCardId, String month, int year);
}
