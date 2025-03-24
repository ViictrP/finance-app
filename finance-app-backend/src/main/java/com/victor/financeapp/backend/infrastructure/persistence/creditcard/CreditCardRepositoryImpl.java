package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
class CreditCardRepositoryImpl implements CreditCardRepository {

    private final CreditCardEntityRepository repository;
    private final CreditCardEntityMapper mapper;

    @Override
    public Flux<CreditCard> findUserCreditCards(Long userId) {
        return repository.findByUserIdAndDeletedIsFalse(userId)
                .map(mapper::toDomain);
    }
}
