package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.InvoiceDomainService;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final MonthClosureRepository monthClosureRepository;
    private final InvoiceDomainService invoiceDomainService;

    @Override
    public Mono<Balance> loadUserBalance(User user, YearMonth yearMonth) {
        return Mono.zip(loadCreditCrds(user, yearMonth).collectList(),
                        loadTransactions(user, yearMonth).collectList(),
                        loadLastFiveAddedTransactions(user).collectList(),
                        findLastFiveMonthClosures(user).collectList())
                .map(tuple -> Balance.builder()
                        .creditCards(tuple.getT1())
                        .transactions(tuple.getT2())
                        .lastAddedTransactions(tuple.getT3())
                        .monthClosures(tuple.getT4())
                        .build());
    }

    private Flux<CreditCard> loadCreditCrds(User user, YearMonth yearMonth) {
        return creditCardRepository.findUserCreditCards(user.getId())
                .flatMap(creditCard -> invoiceDomainService.populateCreditCardInvoice(creditCard, yearMonth));
    }

    private Flux<Transaction> loadTransactions(User user, YearMonth yearMonth) {
        return transactionRepository.findUserTransactionsOn(user.getId(), yearMonth);
    }

    private Flux<Transaction> loadLastFiveAddedTransactions(User user) {
        return transactionRepository.findLastFiveAdded(user.getId());
    }

    private Flux<MonthClosure> findLastFiveMonthClosures(User user) {
        return monthClosureRepository.findUsersLastFiveMonthClosures(user.getId());
    }
}
