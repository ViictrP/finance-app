package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.application.mapper.TransactionMapper;
import com.victor.financeapp.backend.application.usercase.GetInstallmentsUseCase;
import com.victor.financeapp.backend.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetInstallmentsUseCaseImpl implements GetInstallmentsUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Flux<TransactionDTO> execute(String installmentId) {
        return transactionRepository.findInstallments(installmentId)
                .map(transactionMapper::toDTO);
    }
}
