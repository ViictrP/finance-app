package com.victor.financeapp.backend.adapter.in.web.controller;

import com.victor.financeapp.backend.application.dto.CreditCardDTO;
import com.victor.financeapp.backend.application.dto.InvoiceDTO;
import com.victor.financeapp.backend.application.usercase.GetInvoice;
import com.victor.financeapp.backend.application.usercase.SaveCreditCardUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CreditCardController {

    private final GetInvoice useCase;
    private final SaveCreditCardUseCase saveCreditCardUseCase;

    @QueryMapping
    public Mono<InvoiceDTO> findInvoice(@Argument Long creditCardId, @Argument YearMonth yearMonth) {
        return useCase.execute(creditCardId, yearMonth);
    }

    @MutationMapping
    public Mono<CreditCardDTO> saveCreditCard(@Argument CreditCardDTO newCreditCard) {
        return saveCreditCardUseCase.execute(newCreditCard);
    }
}
