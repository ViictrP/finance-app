package com.victor.financeapp.backend.application.usercase;

import reactor.core.publisher.Mono;

public interface DeleteTransactionUseCase {
    Mono<Boolean> execute(Long id, Boolean all);
}
