package com.victor.financeapp.backend.domain.model.user;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Builder
public record Balance(
        List<Transaction> transactions,
        List<Transaction> recurringExpenses,
        List<CreditCard> creditCards,
        BigDecimal salary,
        BigDecimal expenses,
        BigDecimal available,
        BigDecimal taxValue,
        BigDecimal exchangeTaxValue,
        BigDecimal nonConvertedSalary,
        Map<Long, BigDecimal> creditCardExpenses
) {
}
