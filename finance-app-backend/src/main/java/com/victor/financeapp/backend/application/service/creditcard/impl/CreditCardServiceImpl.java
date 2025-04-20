package com.victor.financeapp.backend.application.service.creditcard.impl;

import com.victor.financeapp.backend.application.service.creditcard.CreditCardService;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public Mono<CreditCard> loadCreditCard(Long creditCardId) {
        return creditCardRepository.findCreditCard(creditCardId)
                .flatMap(this::loadInvoices);
    }

    private Mono<CreditCard> loadInvoices(CreditCard creditCard) {
        return invoiceRepository.findCreditCardInvoices(creditCard.getId())
                .collectList()
                .map(invoices -> {
                    creditCard.setInvoices(invoices);
                    return creditCard;
                });
    }
}
