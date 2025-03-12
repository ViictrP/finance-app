package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CreditCardEntityRepository extends ReactiveCrudRepository<CreditCardEntity, Long> {
}
