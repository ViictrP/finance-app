package com.victor.financeapp.backend.application.service.user.impl;

import com.victor.financeapp.backend.application.service.user.BalanceService;
import com.victor.financeapp.backend.application.service.user.InvoiceDomainService;
import com.victor.financeapp.backend.domain.model.user.Balance;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        var balance = Balance.builder();
        return Mono.zip(creditCardRepository.findUserCreditCards(user.getId())
                                .flatMap(creditCard -> invoiceDomainService.populateCreditCardInvoice(creditCard, yearMonth))
                                .collectList(),
                        transactionRepository.findUserTransactionsOn(user.getId(), yearMonth).collectList(),
                        monthClosureRepository.findUsersLastFiveMonthClosures(user.getId()).collectList(),
                        transactionRepository.findAllRecurringExpenses(user.getId()).collectList())
                .map(tuple -> {
                    balance.creditCards(tuple.getT1());
                    balance.transactions(tuple.getT2());
                    balance.monthClosures(tuple.getT3());
                    balance.recurringExpenses(tuple.getT4());
                    return balance.build();
                });
    }
}
