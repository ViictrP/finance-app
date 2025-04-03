package com.victor.financeapp.backend.application.service.user;

import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.model.user.Balance;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface UserDomainService {

    Mono<User> loadUserData(User user, YearMonth yearMonth);
    Mono<Balance> calculateUserBalance(User user);
}
