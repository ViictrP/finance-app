package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Repository
@RequiredArgsConstructor
class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoiceEntityRepository repository;
    private final InvoiceEntityMapper mapper;

    @Override
    public Mono<Invoice> findCreditCardsInvoiceOn(Long creditCardId, YearMonth yearMonth) {
        var month = yearMonth.getMonth().name().substring(0, 3);
        return repository.findByCreditCardIdAndMonthAndYearAndDeletedIsFalse(creditCardId, month, yearMonth.getYear())
                .map(mapper::toDomain);
    }
}
