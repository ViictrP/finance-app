package com.victor.financeapp.backend.infrastructure.persistence.budget;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

interface BudgetCategoryEntityRepository extends ReactiveCrudRepository<BudgetCategoryEntity, Long> {
    Flux<BudgetCategoryEntity> findByBudgetId(Long budgetId);
}