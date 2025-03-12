package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface InvoiceEntityRepository extends ReactiveCrudRepository<InvoiceEntity, Long> {
}
