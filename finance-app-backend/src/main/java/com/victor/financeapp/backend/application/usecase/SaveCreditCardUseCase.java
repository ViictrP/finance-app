package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.CreditCardDTO;
import reactor.core.publisher.Mono;

public interface SaveCreditCardUseCase {

    Mono<CreditCardDTO> execute(CreditCardDTO creditCardDTO);
}
