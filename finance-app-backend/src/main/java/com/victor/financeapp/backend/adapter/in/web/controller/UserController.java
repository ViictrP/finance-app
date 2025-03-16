package com.victor.financeapp.backend.adapter.in.web.controller;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.usercase.GetBalanceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final GetBalanceUseCase getBalanceUseCase;

    @QueryMapping
    public Mono<UserBalanceDTO> getBalance(@Argument YearMonth yearMonth) {
        return getBalanceUseCase.execute(yearMonth);
    }
}
