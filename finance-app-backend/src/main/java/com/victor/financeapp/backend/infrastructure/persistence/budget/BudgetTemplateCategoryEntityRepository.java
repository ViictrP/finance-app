package com.victor.financeapp.backend.infrastructure.persistence.budget;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

interface BudgetTemplateCategoryEntityRepository extends ReactiveCrudRepository<BudgetTemplateCategoryEntity, Long> {
    Flux<BudgetTemplateCategoryEntity> findByTemplateId(Long templateId);
}
