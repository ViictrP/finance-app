package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.BudgetTemplateDTO;
import reactor.core.publisher.Mono;

public interface GetBudgetTemplateUseCase {

    Mono<BudgetTemplateDTO> execute();
}
