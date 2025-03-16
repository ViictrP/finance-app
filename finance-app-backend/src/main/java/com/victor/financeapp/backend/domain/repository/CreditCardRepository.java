package com.victor.financeapp.backend.domain.repository;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import reactor.core.publisher.Flux;

public interface CreditCardRepository {
    Flux<CreditCard> findUserCreditCards(Long userId);
}
