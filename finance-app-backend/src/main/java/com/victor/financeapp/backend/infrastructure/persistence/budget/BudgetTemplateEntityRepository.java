package com.victor.financeapp.backend.infrastructure.persistence.budget;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

interface BudgetTemplateEntityRepository extends ReactiveCrudRepository<BudgetTemplateEntity, Long> {
    Mono<BudgetTemplateEntity> findByUserId(Long userId);
}
