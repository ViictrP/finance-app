package com.victor.financeapp.backend.application.service.user;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface InvoiceDomainService {

    Mono<CreditCard> populateCreditCardInvoice(CreditCard creditCard, YearMonth yearMonth);
}
