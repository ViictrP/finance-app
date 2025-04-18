package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
interface CreditCardEntityRepository extends ReactiveCrudRepository<CreditCardEntity, Long> {
    Flux<CreditCardEntity> findByUserIdAndDeletedIsFalseOrderByTitle(Long userId);
}
