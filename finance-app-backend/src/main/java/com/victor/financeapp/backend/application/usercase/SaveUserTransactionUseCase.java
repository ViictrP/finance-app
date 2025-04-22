package com.victor.financeapp.backend.application.usercase;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface SaveUserTransactionUseCase {

    Mono<TransactionDTO> execute(TransactionDTO dto);
}
