package com.victor.financeapp.backend.domain.exception;

import java.time.YearMonth;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(Long creditCardId, YearMonth yearMonth) {
        super("CreditCard[%s]: A invoice de %s não foi encontrada".formatted(creditCardId, yearMonth));
    }
}
