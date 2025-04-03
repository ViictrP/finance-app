package com.victor.financeapp.backend.application.usercase;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface GetBalanceUseCase {
    Mono<UserBalanceDTO> execute(YearMonth yearMonth);
}
