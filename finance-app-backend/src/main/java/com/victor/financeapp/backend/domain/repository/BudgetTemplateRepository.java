package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.budget.BudgetTemplate;
import reactor.core.publisher.Mono;

public interface BudgetTemplateRepository {
    Mono<BudgetTemplate> save(BudgetTemplate template);
    Mono<BudgetTemplate> findByUserId(Long userId);
}
