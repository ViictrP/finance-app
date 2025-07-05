package com.victor.financeapp.backend.application.usecase.impl;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.mapper.TransactionMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usecase.SaveUserTransactionUseCase;
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
        var transaction = mapper.toDomain(dto);

        return userService.getLoggedInUser()
                .flatMap(user -> {
                    log.info("Saving user {} transaction {}", user.getName(), transaction.getDescription());
                    return transactionRepository.save(user.addTransaction(transaction))
                            .doOnNext(tr -> log.info("Transaction {} saved!", tr.getDescription()));
                })
                .map(mapper::toDTO)
                .doOnNext(saved -> log.info("Processing transaction {} is completed", saved.description()));
    }
}
