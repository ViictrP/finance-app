package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.InvoiceDTO;
import com.victor.financeapp.backend.application.mapper.InvoiceMapper;
import com.victor.financeapp.backend.application.service.user.InvoiceDomainService;
import com.victor.financeapp.backend.application.usercase.GetInvoice;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetInvoiceImpl implements GetInvoice {

    private final CreditCardRepository creditCardRepository;
    private final InvoiceDomainService service;
    private final InvoiceMapper mapper;

    @Override
    public Mono<InvoiceDTO> execute(Long creditCardId, YearMonth yearMonth) {
        return creditCardRepository.findCreditCard(creditCardId)
                .flatMap(creditCard -> service.populateCreditCardInvoice(creditCard, yearMonth))
                .map(creditCard -> creditCard.getInvoice(yearMonth))
                .map(mapper::toDTO);
    }
}
