package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.currency.CurrencyService;
import com.victor.financeapp.backend.application.service.user.UserDomainService;
import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    private final CurrencyService currencyService;

    @Override
    public Mono<Balance> calculateUserBalance(User user) {
        return currencyService.getDollarExchangeRates()
                .doOnNext(user::calculateBalance)
                .thenReturn(user.getBalance());
    }
}
