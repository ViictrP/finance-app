package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.mapper.BalanceMapper;
import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.UserDomainService;
import com.victor.financeapp.backend.application.usercase.GetBalanceUseCase;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.infrastructure.security.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserBalanceUseCaseImpl implements GetBalanceUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final BalanceService balanceService;

    private final BalanceMapper balanceMapper;

    @Override
    public Mono<UserBalanceDTO> execute(YearMonth yearMonth) {
        return SecurityContext.getUserEmail()
                .flatMap(email -> loadUser(yearMonth, email));
    }

    private Mono<UserBalanceDTO> loadUser(YearMonth yearMonth, String userEmail) {
        return userRepository.findUser(userEmail)
                .flatMap(user -> loadUserBalance(yearMonth, user))
                .flatMap(userDomainService::calculateUserBalance)
                .map(balanceMapper::toDTO);
    }

    private Mono<User> loadUserBalance(YearMonth yearMonth, User user) {
        return this.balanceService.loadUserBalance(user, yearMonth)
                .map(balance -> {
                    user.setBalance(balance);
                    return user;
                });
    }
}
