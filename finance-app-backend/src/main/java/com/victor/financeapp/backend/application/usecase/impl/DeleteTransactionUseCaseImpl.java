package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.DeleteTransactionUseCase;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTransactionUseCaseImpl implements DeleteTransactionUseCase {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    @Override
    public Mono<Boolean> execute(Long id, Boolean all) {
        return userService.getLoggedInUser()
                .flatMap(user -> {
                    if (all) {
                        return transactionRepository.findById(id)
                                .flatMap(transaction ->
                                        transactionRepository.deleteByInstallmentIdAndUserId(transaction.getInstallmentId(), user.getId()));
                    } else {
                        return transactionRepository.deleteByIdAndUserId(id, user.getId());
                    }
                });
    }
}
