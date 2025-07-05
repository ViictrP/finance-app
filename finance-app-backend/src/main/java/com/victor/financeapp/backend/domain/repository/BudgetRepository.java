package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.budget.Budget;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface BudgetRepository {
    Mono<Budget> save(Budget budget);
    Mono<Budget> findByUserIdAndMonth(Long userId, YearMonth month);
}