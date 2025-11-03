package com.victor.financeapp.backend.infrastructure.client.currency;

import com.victor.financeapp.backend.application.service.currency.client.CurrencyClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyClientImpl implements CurrencyClient {

    @Value("${CURRENCY.url}")
    private String currencyUrl;

    @Value("${CURRENCY.token}")
    private String awesomeAPIToken;

    private final WebClient webClient;

    @Override
    @SneakyThrows
    public Mono<Map<String, Object>> getCurrencyExchangeRates(String currencyType) {
        return webClient.
                get()
                .uri(currencyUrl + "/" + currencyType)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Erro ao chamar API"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Erro ao chamar API"))
                )
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
