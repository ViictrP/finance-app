package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.mapper.BalanceMapper;
import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.GetBalanceUseCase;
import com.victor.financeapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserBalanceUseCaseImpl implements GetBalanceUseCase {

    private final UserService userService;
    private final BalanceService balanceService;

    private final BalanceMapper balanceMapper;

    @Override
    public Mono<UserBalanceDTO> execute(YearMonth yearMonth) {
        return userService.getLoggedInUser()
                .flatMap(user -> loadUserBalance(yearMonth, user))
                .flatMap(userService::calculateUserBalance)
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
