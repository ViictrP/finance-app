package com.victor.financeapp.backend.application.usercase;

import reactor.core.publisher.Mono;

public interface SaveMonthClosureUseCase {
    Mono<Void> execute();
}
