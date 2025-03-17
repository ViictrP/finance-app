package com.victor.financeapp.backend.application.dto;

import com.victor.financeapp.backend.domain.model.Transaction.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record TransactionDTO(
        Long id,
        String description,
        BigDecimal amount,
        TransactionType type,
        OffsetDateTime date,
        Boolean isInstallment,
        Integer installmentAmount,
        String installmentId,
        Integer installmentNumber,
        Long creditCardId
) {
}
