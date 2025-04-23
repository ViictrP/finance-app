package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.mapper.TransactionMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.SaveUserTransactionUseCase;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.user.User;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveUserTransactionUseCaseImpl implements SaveUserTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper mapper;

    @Override
    @Transactional
    public Mono<TransactionDTO> execute(TransactionDTO dto) {
        log.info("Processing new transaction {}", dto.description());
        return userService.getLoggedInUser()
                .flatMap(user -> saveNewTransaction(dto, user))
                .map(mapper::toDTO)
                .doOnNext(saved -> log.info("Processing transaction {} is completed", saved.description()));
    }

    private Mono<Transaction> saveNewTransaction(TransactionDTO dto, User user) {
        log.info("Saving user {} transaction {}", user.getName(), dto.description());
        return Mono.just(dto)
                .map(mapper::toDomain)
                .flatMap(transaction -> {
                    user.addTransaction(transaction);
                    return transactionRepository.save(transaction)
                            .doOnNext(tr -> log.info("Transaction {} saved!", tr.getDescription()));
                });
    }
}
