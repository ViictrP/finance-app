package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.BudgetPerformanceDTO;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface GetBudgetPerformanceUseCase {
    Mono<BudgetPerformanceDTO> execute(YearMonth month);
}
