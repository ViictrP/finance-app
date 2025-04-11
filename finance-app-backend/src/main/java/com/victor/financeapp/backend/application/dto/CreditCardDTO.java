package com.victor.financeapp.backend.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CreditCardDTO(
        Long id,
        Long userId,
        String title,
        String description,
        String number,
        String color,
        Integer invoiceClosingDay,
        BigDecimal totalInvoiceAmount,
        List<InvoiceDTO> invoices
) {
}
