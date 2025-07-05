package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import reactor.core.publisher.Flux;

public interface GetInstallmentsUseCase {

    Flux<TransactionDTO> execute(String installmentId);
}
