package com.victor.financeapp.backend.application.service.currency.impl;

import com.victor.financeapp.backend.application.service.currency.CurrencyService;
import com.victor.financeapp.backend.application.service.currency.client.CurrencyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyClient client;

    @Override
    public Mono<BigDecimal> getDollarExchangeRates() {
        log.info("Retrieving dollar exchange rates");
        return client.getCurrencyExchangeRates("USD-BRLPTAX")
                .map(response -> (Map<String, String>) response.get("USDBRLPTAX"))
                .map(currency -> currency.get("ask"))
                .map(BigDecimal::new)
                .doOnSuccess(value -> log.info("Currency exchange rates {}", value));
    }
}
