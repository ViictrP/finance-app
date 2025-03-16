package com.victor.financeapp.backend.application.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreditCardDTO(
        Long id,
        List<InvoiceDTO> invoices
) {
}
