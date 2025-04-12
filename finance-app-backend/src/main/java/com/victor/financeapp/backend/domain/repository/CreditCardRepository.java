package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardRepository {
    Flux<CreditCard> findUserCreditCards(Long userId);

    Mono<CreditCard> findCreditCard(Long creditCardId);
}
