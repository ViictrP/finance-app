package com.victor.financeapp.backend.application.service.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import reactor.core.publisher.Mono;

public interface CreditCardService {

    Mono<CreditCard> loadCreditCard(Long creditCardId);
}
