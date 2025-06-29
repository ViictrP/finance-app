package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.infrastructure.security.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    
    private final UserRepository userRepository;

    

    @Override
    public Mono<User> getLoggedInUser() {
        return SecurityContext.getUserEmail()
                .flatMap(this::loadUser);
    }

    @Override
    public Flux<User> findUsersWithMonthClosureToday() {
        return userRepository.findUsersWithMonthClosureToday();
    }

    private Mono<User> loadUser(String userEmail) {
        return userRepository.findUser(userEmail);
    }
}
