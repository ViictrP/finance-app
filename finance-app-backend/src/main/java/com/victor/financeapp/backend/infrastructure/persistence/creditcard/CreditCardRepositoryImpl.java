package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<CreditCard> findCreditCard(Long creditCardId) {
        return repository.findById(creditCardId)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<CreditCard> save(CreditCard creditCard) {
        return repository.save(mapper.toEntity(creditCard))
                .map(mapper::toDomain);
    }
}
