package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import reactor.core.publisher.Mono;

public interface SaveBudgetUseCase {
    Mono<BudgetDTO> execute(BudgetDTO budget);
}