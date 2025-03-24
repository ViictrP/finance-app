package com.victor.financeapp.backend.application.service.currency.client;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface CurrencyClient {
    Mono<Map<String, Object>> getCurrencyExchangeRates(String currencyType);
}
