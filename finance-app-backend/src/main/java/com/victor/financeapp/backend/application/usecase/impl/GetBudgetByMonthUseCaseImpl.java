package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import com.victor.financeapp.backend.application.mapper.BudgetMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.GetBudgetByMonthUseCase;
import com.victor.financeapp.backend.domain.model.budget.Budget;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.BudgetRepository;
import com.victor.financeapp.backend.domain.repository.BudgetTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class GetBudgetByMonthUseCaseImpl implements GetBudgetByMonthUseCase {

    private final BudgetRepository budgetRepository;
    private final BudgetTemplateRepository budgetTemplateRepository;
    private final UserService userService;
    private final BudgetMapper mapper;

    @Override
    public Mono<BudgetDTO> execute(YearMonth month) {
        return userService.getLoggedInUser()
                .flatMap(user ->
                        budgetRepository.findByUserIdAndMonth(user.getId(), month)
                                .switchIfEmpty(createBudgetFromTemplate(user, month))
                )
                .map(mapper::toDto);
    }

    private Mono<Budget> createBudgetFromTemplate(User user, YearMonth month) {
        return budgetTemplateRepository.findByUserId(user.getId())
                .flatMap(template -> {
                    var newBudget = template.createBudgetForMonth(month);
                    return budgetRepository.save(newBudget);
                });
    }
}