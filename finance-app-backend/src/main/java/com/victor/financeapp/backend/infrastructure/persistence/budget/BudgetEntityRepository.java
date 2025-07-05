package com.victor.financeapp.backend.infrastructure.persistence.budget;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

interface BudgetEntityRepository extends ReactiveCrudRepository<BudgetEntity, Long> {
    Mono<BudgetEntity> findByUserIdAndMonth(Long userId, String month);
}