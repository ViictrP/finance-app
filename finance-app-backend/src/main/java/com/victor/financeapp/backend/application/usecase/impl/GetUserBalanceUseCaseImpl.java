package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.mapper.BalanceMapper;
import com.victor.financeapp.backend.application.service.currency.CurrencyService;
import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.GetBalanceUseCase;
import com.victor.financeapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserBalanceUseCaseImpl implements GetBalanceUseCase {

    private final UserService userService;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;

    private final BalanceMapper balanceMapper;

    @Override
    public Mono<UserBalanceDTO> execute(YearMonth yearMonth) {
        Mono<User> userMono = userService.getLoggedInUser().cache();
        Mono<BigDecimal> dollarRateMono = currencyService.getDollarExchangeRates().cache();

        return userMono.zipWith(dollarRateMono)
                .flatMap(tuple -> {
                    var user = tuple.getT1();
                    var dollarRate = tuple.getT2();
                    return loadUserBalance(yearMonth, user)
                            .map(loadedUser -> {
                                loadedUser.calculateBalance(dollarRate);
                                return loadedUser;
                            });
                })
                .map(User::getBalance)
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
