package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import reactor.core.publisher.Mono;

public interface SaveCreditCardTransactionUseCase {

    Mono<TransactionDTO> execute(TransactionDTO transactionDTO);
}
