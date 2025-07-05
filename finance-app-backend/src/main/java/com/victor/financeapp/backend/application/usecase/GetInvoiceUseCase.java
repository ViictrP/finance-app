package com.victor.financeapp.backend.application.usecase;

import com.victor.financeapp.backend.application.dto.InvoiceDTO;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface GetInvoiceUseCase {

    Mono<InvoiceDTO> execute(Long creditCardId, YearMonth yearMonth);
}
