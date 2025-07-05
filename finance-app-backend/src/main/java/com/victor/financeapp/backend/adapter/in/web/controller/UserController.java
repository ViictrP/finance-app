package com.victor.financeapp.backend.adapter.in.web.controller;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.usecase.GetBalanceUseCase;
import com.victor.financeapp.backend.application.usecase.SaveUserTransactionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final GetBalanceUseCase getBalanceUseCase;
    private final SaveUserTransactionUseCase saveUserTransactionUseCase;

    @QueryMapping
    public Mono<UserBalanceDTO> getBalance(@Argument YearMonth yearMonth) {
        return getBalanceUseCase.execute(yearMonth);
    }

    @MutationMapping
    public Mono<TransactionDTO> saveTransaction(@Argument TransactionDTO newTransaction) {
        return saveUserTransactionUseCase.execute(newTransaction);
    }
}
