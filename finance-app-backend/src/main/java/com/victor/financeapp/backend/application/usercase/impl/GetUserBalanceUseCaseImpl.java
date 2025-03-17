package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.application.mapper.UserMapper;
import com.victor.financeapp.backend.application.usercase.GetBalanceUseCase;
import com.victor.financeapp.backend.domain.model.MonthClosure;
import com.victor.financeapp.backend.domain.model.Transaction;
import com.victor.financeapp.backend.domain.model.User;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.MonthClosureRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import com.victor.financeapp.backend.domain.repository.UserRepository;
import com.victor.financeapp.backend.infrastructure.security.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserBalanceUseCaseImpl implements GetBalanceUseCase {

    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final MonthClosureRepository monthClosureRepository;
    private final InvoiceRepository invoiceRepository;

    private final UserMapper userMapper;

    @Override
    public Mono<UserBalanceDTO> execute(YearMonth yearMonth) {
        return loadUser(yearMonth, "vpradodev@gmail.com");
    }

    private Mono<UserBalanceDTO> loadUser(YearMonth yearMonth, String userEmail) {
        return userRepository.findUser(userEmail)
                .flatMap(user -> this.loadUserData(user, yearMonth))
                .map(userMapper::toBalanceDTO)
                .map(this::completeBalanceData);
    }

    private Mono<User> loadUserData(User user, YearMonth yearMonth) {
        Mono<List<CreditCard>> creditCardsMono = creditCardRepository.findUserCreditCards(user.getId())
                .flatMap(creditCard -> populateCreditCardInvoice(yearMonth, creditCard))
                .collectList();

        Mono<List<Transaction>> transactionsMono = transactionRepository.findUserTransactionsOn(user.getId(), yearMonth)
                .collectList();

        Mono<List<MonthClosure>> monthClosuresMono = monthClosureRepository.findUsersLastFiveMonthClosures(user.getId())
                .collectList();

        return Mono.zip(creditCardsMono, transactionsMono, monthClosuresMono)
                .doOnNext(tuple -> {
                    user.addCreditCards(tuple.getT1());
                    user.addTransactions(tuple.getT2());
                    user.addMonthClosures(tuple.getT3());
                })
                .thenReturn(user);
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

    private UserBalanceDTO completeBalanceData(UserBalanceDTO userBalanceDTO) {
        return userBalanceDTO;
    }
}
