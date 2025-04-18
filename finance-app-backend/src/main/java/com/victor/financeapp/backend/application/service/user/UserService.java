package com.victor.financeapp.backend.application.service.user;

import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Balance> calculateUserBalance(User user);
    Mono<User> getLoggedInUser();
}
