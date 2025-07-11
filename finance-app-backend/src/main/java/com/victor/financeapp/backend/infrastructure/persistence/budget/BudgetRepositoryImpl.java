package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.Budget;
import com.victor.financeapp.backend.domain.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Repository
@RequiredArgsConstructor
class BudgetRepositoryImpl implements BudgetRepository {

    private final BudgetEntityRepository budgetEntityRepository;
    private final BudgetCategoryEntityRepository budgetCategoryEntityRepository;
    private final BudgetEntityMapper mapper;
    private final BudgetCategoryEntityMapper categoryMapper;

    @Override
    @Transactional
    public Mono<Budget> save(Budget budget) {
        var budgetEntity = mapper.toEntity(budget);

        Mono<BudgetEntity> savedBudgetEntityMono;
        if (budgetEntity.getId() == null) {
            savedBudgetEntityMono = budgetEntityRepository.save(budgetEntity);
        } else {
            savedBudgetEntityMono = Mono.just(budgetEntity);
        }

        return savedBudgetEntityMono.flatMap(savedBudgetEntity -> {
            var categoryEntities = budget.getCategories().stream()
                    .map(category -> {
                        var categoryEntity = categoryMapper.toEntity(category);
                        categoryEntity.setBudgetId(savedBudgetEntity.getId());
                        return categoryEntity;
                    })
                    .toList();

            return budgetCategoryEntityRepository.saveAll(categoryEntities)
                    .collectList()
                    .map(savedCategoryEntities -> {
                        var savedBudget = mapper.toDomain(savedBudgetEntity);
                        savedBudget.setCategories(
                                savedCategoryEntities.stream()
                                        .map(categoryMapper::toDomain)
                                        .toList());
                        return savedBudget;
                    });
        });
    }

    @Override
    public Mono<Budget> findByUserIdAndMonth(Long userId, YearMonth month) {
        return budgetEntityRepository.findByUserIdAndMonth(userId, mapper.fromYearMonth(month))
                .flatMap(budgetEntity ->
                        budgetCategoryEntityRepository.findByBudgetId(budgetEntity.getId())
                                .map(categoryMapper::toDomain)
                                .collectList()
                                .map(categories -> {
                                    var budget = mapper.toDomain(budgetEntity);
                                    budget.setCategories(categories);
                                    return budget;
                                })
                );
    }
}