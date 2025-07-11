package com.victor.financeapp.backend.application.dto;

import com.victor.financeapp.backend.domain.model.common.Transaction.TransactionType;
import lombok.Builder;

import com.victor.financeapp.backend.domain.model.common.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        String description,
        BigDecimal amount,
        String type,
        LocalDateTime date,
        Category category,
        boolean isInstallment,
        BigDecimal installmentAmount,
        String installmentId,
        int installmentNumber,
        Long creditCardId
) {
}

