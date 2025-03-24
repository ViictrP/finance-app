package com.victor.financeapp.backend.application.service.currency;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CurrencyService {
    Mono<BigDecimal> getDollarExchangeRates();
}
