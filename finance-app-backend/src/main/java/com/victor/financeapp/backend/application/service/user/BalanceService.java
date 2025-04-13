package com.victor.financeapp.backend.application.service.user;

import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.User;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface BalanceService {

    Mono<Balance> loadUserBalance(User user, YearMonth yearMonth);
}
