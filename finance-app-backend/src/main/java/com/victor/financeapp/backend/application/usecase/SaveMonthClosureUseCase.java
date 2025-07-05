package com.victor.financeapp.backend.application.usecase;

import reactor.core.publisher.Mono;

public interface SaveMonthClosureUseCase {
    Mono<Void> execute();
}
