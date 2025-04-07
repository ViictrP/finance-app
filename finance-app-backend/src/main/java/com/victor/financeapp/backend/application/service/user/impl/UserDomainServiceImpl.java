package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.currency.CurrencyService;
import com.victor.financeapp.backend.application.service.user.UserDomainService;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final MonthClosureRepository monthClosureRepository;
    private final InvoiceRepository invoiceRepository;

    private final CurrencyService currencyService;

    public Mono<User> loadUserData(User user, YearMonth yearMonth) {
        return Mono.zip(creditCardRepository.findUserCreditCards(user.getId())
                                .flatMap(creditCard -> populateCreditCardInvoice(yearMonth, creditCard))
                                .collectList(),
                        transactionRepository.findUserTransactionsOn(user.getId(), yearMonth).collectList(),
                        monthClosureRepository.findUsersLastFiveMonthClosures(user.getId()).collectList(),
                        transactionRepository.findAllRecurringExpenses(user.getId()).collectList())
                .doOnNext(tuple -> {
                    user.addCreditCards(tuple.getT1());
                    user.addTransactions(tuple.getT2());
                    user.addMonthClosures(tuple.getT3());
                    user.addRecurringExpenses(tuple.getT4());
                })
                .thenReturn(user);
    }

    @Override
    public Mono<Balance> calculateUserBalance(User user) {
        return currencyService.getDollarExchangeRates()
                .map(user::calculateBalance);
    }

    private Mono<CreditCard> populateCreditCardInvoice(YearMonth yearMonth, CreditCard creditCard) {
        return invoiceRepository.findCreditCardsInvoiceOn(creditCard.getId(), yearMonth)
                .flatMap(this::populateInvoiceTransactions)
                .flatMap(invoice -> {
                    creditCard.addInvoice(invoice);
                    return Mono.just(creditCard);
                });
    }

    private Mono<Invoice> populateInvoiceTransactions(Invoice invoice) {
        return transactionRepository.findInvoiceTransactionsOn(invoice.getId())
                .collectList()
                .doOnNext(invoice::addTransactions)
                .thenReturn(invoice);
    }
}
