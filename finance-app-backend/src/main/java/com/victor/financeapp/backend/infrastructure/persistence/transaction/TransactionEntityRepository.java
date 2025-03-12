package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TransactionEntityRepository extends ReactiveCrudRepository<TransactionEntity, Long> {
}
