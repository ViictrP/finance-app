package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findUser(String email);
}
