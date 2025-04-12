package com.victor.financeapp.backend.application.dto;

import java.time.YearMonth;
import java.util.List;

public record InvoiceDTO(
        Long id,
        Long creditCardId,
        Boolean isClosed,
        YearMonth yearMonth,
        List<TransactionDTO> transactions
) {
}
