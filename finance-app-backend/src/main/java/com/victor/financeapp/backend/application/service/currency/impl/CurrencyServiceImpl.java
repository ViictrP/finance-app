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
        return client.getCurrencyExchangeRates("USD-BRL")
                .map(CurrencyServiceImpl::getBigDecimalCurrency)
                .doOnError(throwable -> log.error(throwable.getLocalizedMessage(), throwable))
                .doOnSuccess(value -> log.info("Currency exchange rates {}", value))
                .onErrorResume(throwable -> Mono.just(BigDecimal.ZERO));
    }

    private static BigDecimal getBigDecimalCurrency(Map<String, Object> response) {
        var usdBrl = (Map<String, String>) response.get("USDBRL");
        var currency = usdBrl.get("ask");
        return new BigDecimal(currency);
    }
}
