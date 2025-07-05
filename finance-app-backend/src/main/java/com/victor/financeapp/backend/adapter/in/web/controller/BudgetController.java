package com.victor.financeapp.backend.adapter.in.web.controller;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import com.victor.financeapp.backend.application.dto.BudgetPerformanceDTO;
import com.victor.financeapp.backend.application.usecase.GetBudgetByMonthUseCase;
import com.victor.financeapp.backend.application.usecase.GetBudgetPerformanceUseCase;
import com.victor.financeapp.backend.application.usecase.SaveBudgetUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Controller
@RequiredArgsConstructor
public class BudgetController {

    private final SaveBudgetUseCase saveBudgetUseCase;
    private final GetBudgetByMonthUseCase getBudgetByMonthUseCase;
    private final GetBudgetPerformanceUseCase getBudgetPerformanceUseCase;

    @QueryMapping
    public Mono<BudgetDTO> getBudgetByMonth(@Argument YearMonth month) {
        return getBudgetByMonthUseCase.execute(month);
    }

    @QueryMapping
    public Mono<BudgetPerformanceDTO> getBudgetPerformance(@Argument YearMonth month) {
        return getBudgetPerformanceUseCase.execute(month);
    }

    @MutationMapping
    public Mono<BudgetDTO> saveBudget(@Argument("budget") BudgetDTO budget) {
        return saveBudgetUseCase.execute(budget);
    }
}
