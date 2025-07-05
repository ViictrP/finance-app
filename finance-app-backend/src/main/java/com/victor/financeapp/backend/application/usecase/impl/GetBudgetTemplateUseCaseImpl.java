package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.BudgetTemplateDTO;
import com.victor.financeapp.backend.application.mapper.BudgetTemplateMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.GetBudgetTemplateUseCase;
import com.victor.financeapp.backend.domain.repository.BudgetTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetBudgetTemplateUseCaseImpl implements GetBudgetTemplateUseCase {

    private final UserService userService;
    private final BudgetTemplateRepository budgetTemplateRepository;
    private final BudgetTemplateMapper budgetTemplateMapper;

    @Override
    public Mono<BudgetTemplateDTO> execute() {
        return userService.getLoggedInUser()
                .flatMap(user -> budgetTemplateRepository.findByUserId(user.getId()))
                .map(budgetTemplateMapper::toDTO);
    }
}
