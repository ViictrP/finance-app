package com.victor.financeapp.backend.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record UserBalanceDTO(
        List<TransactionDTO> transactions,
        List<CreditCardDTO> creditCards,
        List<MonthClosureDTO> monthClosures,
        BigDecimal salary,
        BigDecimal expenses,
        BigDecimal available,
        BigDecimal taxValue,
        BigDecimal exchangeTaxValue,
        BigDecimal nonConvertedSalary,
        Map<Long, BigDecimal> creditCardExpenses
) {
}
