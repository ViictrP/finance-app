package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import com.victor.financeapp.backend.application.mapper.BudgetMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.SaveBudgetUseCase;
import com.victor.financeapp.backend.domain.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SaveBudgetUseCaseImpl implements SaveBudgetUseCase {

    private final BudgetRepository budgetRepository;
    private final UserService userService;
    private final BudgetMapper mapper;

    @Override
    @Transactional
    public Mono<BudgetDTO> execute(BudgetDTO dto) {
        return userService.getLoggedInUser()
                .flatMap(user -> {
                    var budget = mapper.toDomain(dto);
                    user.addBudget(budget);
                    return budgetRepository.save(budget);
                })
                .map(mapper::toDto);
    }
}