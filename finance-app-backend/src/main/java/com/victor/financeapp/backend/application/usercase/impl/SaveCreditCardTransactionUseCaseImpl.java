package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.mapper.TransactionMapper;
import com.victor.financeapp.backend.application.service.creditcard.CreditCardService;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.SaveCreditCardTransactionUseCase;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.InvoiceRepository;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
        return userService.getLoggedInUser()
                .flatMap(user -> createAndSaveInstallments(transactionDTO, user))
                .map(mapper::toDTO)
                .doOnNext(saved -> log.info("Processing transaction {} has completed", saved.description()));
    }

    private Mono<Transaction> createAndSaveInstallments(TransactionDTO dto, User user) {
        var transaction = mapper.toDomain(dto);

        var installments = transaction.getInstallmentTransactions();
        log.info("Creating {} installment transaction(s)", installments.size());

        log.info("Loading the credit card {}", dto.creditCardId());
        return creditCardService.loadCreditCard(dto.creditCardId())
                .flatMapMany(creditCard -> Flux.fromIterable(installments)
                        .flatMap(tr -> {
                            creditCard.addTransaction(tr);
                            user.addTransaction(tr);
                            return saveTransaction(tr)
                                    .doOnNext(saved -> log.info("Saved transaction {} for user {} and card {}", saved.getId(), user.getId(), creditCard.getId()));
                        }))
                .collectList()
                .map(List::getFirst);
    }

    private Mono<Transaction> saveTransaction(Transaction transaction) {
        var invoice = transaction.getInvoice();

        if (invoice.isNew()) {
            return invoiceRepository.save(invoice)
                    .doOnNext(saved -> log.info("New invoice created on {}", invoice.getYearMonth()))
                    .flatMap(i -> {
                        i.addTransaction(transaction);
                        return transactionRepository.save(transaction);
                    });
        } else {
            return transactionRepository.save(transaction);
        }
    }
}
