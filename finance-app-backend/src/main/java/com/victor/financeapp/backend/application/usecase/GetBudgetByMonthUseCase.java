package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface GetBudgetByMonthUseCase {
    Mono<BudgetDTO> execute(YearMonth month);
}