package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.mapper.TransactionMapper;
import com.victor.financeapp.backend.application.service.creditcard.CreditCardService;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.SaveCreditCardTransactionUseCase;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveCreditCardTransactionUseCaseImpl implements SaveCreditCardTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final InvoiceRepository invoiceRepository;

    private final CreditCardService creditCardService;
    private final UserService userService;

    private final TransactionMapper mapper;

    @Override
    @Transactional
    public Mono<TransactionDTO> execute(TransactionDTO transactionDTO) {
        log.info("Processing new transaction {} for credit card {}", transactionDTO.description(), transactionDTO.creditCardId());

        var originalTransaction = mapper.toDomain(transactionDTO);

        Mono<User> userMono = userService.getLoggedInUser().cache();
        Mono<CreditCard> creditCardMono = creditCardService.loadCreditCard(transactionDTO.creditCardId()).cache();

        return userMono.zipWith(creditCardMono)
                .flatMap(tuple -> {
                    var user = tuple.getT1();
                    var creditCard = tuple.getT2();

                    var installments = creditCard.createInstallmentTransactions(originalTransaction);
                    log.info("Creating {} installment transaction(s)", installments.size());

                    return Flux.fromIterable(installments)
                            .flatMap(installment -> {
                                creditCard.addTransaction(installment);
                                installment.setDate(originalTransaction.getDate());
                                return saveTransaction(installment, user, creditCard);
                            })
                            .then(Mono.just(originalTransaction));
                })
                .map(mapper::toDTO)
                .doOnNext(saved -> log.info("Processing transaction {} has completed", saved.description()));
    }

    private Mono<Transaction> saveTransaction(Transaction transaction, User user, CreditCard creditCard) {
        var invoice = transaction.getInvoice();

        Mono<Transaction> saveTransactionMono = transactionRepository.save(transaction)
                .doOnNext(saved -> log.info("Saved transaction {} for user {} and card {}", saved.getId(), user.getId(), creditCard.getId()));

        if (invoice.isNew()) {
            return invoiceRepository.save(invoice)
                    .doOnNext(saved -> log.info("New invoice created on {}", invoice.getYearMonth()))
                    .flatMap(i -> {
                        i.addTransaction(transaction);
                        return saveTransactionMono;
                    });
        } else {
            return saveTransactionMono;
        }
    }


}
