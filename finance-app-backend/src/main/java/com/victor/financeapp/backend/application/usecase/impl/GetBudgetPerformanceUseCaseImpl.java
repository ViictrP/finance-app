package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.BudgetPerformanceCategoryDTO;
import com.victor.financeapp.backend.application.dto.BudgetPerformanceDTO;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.GetBudgetPerformanceUseCase;
import com.victor.financeapp.backend.domain.model.budget.Budget;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.repository.BudgetRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetBudgetPerformanceUseCaseImpl implements GetBudgetPerformanceUseCase {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Override
    public Mono<BudgetPerformanceDTO> execute(YearMonth month) {
        return userService.getLoggedInUser()
                .flatMap(user -> {
                    Mono<Budget> budgetMono = budgetRepository.findByUserIdAndMonth(user.getId(), month).cache();
                    Mono<List<Transaction>> transactionsMono = transactionRepository.findByUserIdAndDateBetween(
                            user.getId(),
                            month.atDay(1).atStartOfDay(),
                            month.atEndOfMonth().atTime(23, 59, 59)
                    ).collectList().cache();

                    return Mono.zip(budgetMono, transactionsMono)
                            .map(tuple -> buildPerformanceDto(month, tuple.getT1(), tuple.getT2()));
                });
    }

    private BudgetPerformanceDTO buildPerformanceDto(YearMonth month, Budget budget, List<Transaction> transactions) {
        Map<String, BigDecimal> spentByCategory = transactions.stream()
                .filter(t -> t.getCategory() != null && t.getAmount().compareTo(BigDecimal.ZERO) > 0) // Consider only expenses
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        BigDecimal totalSpent = spentByCategory.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BudgetPerformanceCategoryDTO> categoryPerformances = budget.getCategories().stream()
                .map(budgetCategory -> {
                    BigDecimal spent = spentByCategory.getOrDefault(budgetCategory.getName(), BigDecimal.ZERO);
                    return new BudgetPerformanceCategoryDTO(
                            budgetCategory.getName(),
                            budgetCategory.getBudgetedAmount(),
                            spent,
                            budgetCategory.getBudgetedAmount().subtract(spent)
                    );
                }).toList();

        return new BudgetPerformanceDTO(
                month,
                budget.getTotalAmount(),
                totalSpent,
                budget.getTotalAmount().subtract(totalSpent),
                categoryPerformances
        );
    }
}
