package com.victor.financeapp.backend.application.dto;

import java.util.List;

public record InvoiceDTO(
        Long id,
        Long creditCardId,
        List<TransactionDTO> transactions
) {
}
