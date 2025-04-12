package com.victor.financeapp.backend.application.usercase;

import com.victor.financeapp.backend.application.dto.InvoiceDTO;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface GetInvoice {

    Mono<InvoiceDTO> execute(Long creditCardId, YearMonth yearMonth);
}
