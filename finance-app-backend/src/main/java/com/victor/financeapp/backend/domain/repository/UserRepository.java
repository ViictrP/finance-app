package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findUser(String email);
    Flux<User> findUsersWithMonthClosureToday();
}
