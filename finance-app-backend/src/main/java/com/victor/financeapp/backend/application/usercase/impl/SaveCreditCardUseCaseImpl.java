package com.victor.financeapp.backend.application.usercase.impl;

import com.victor.financeapp.backend.application.dto.CreditCardDTO;
import com.victor.financeapp.backend.application.mapper.CreditCardMapper;
import com.victor.financeapp.backend.application.service.user.UserService;
import com.victor.financeapp.backend.application.usercase.SaveCreditCardUseCase;
import com.victor.financeapp.backend.domain.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SaveCreditCardUseCaseImpl implements SaveCreditCardUseCase {

    private final UserService userService;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardMapper creditCardMapper;

    @Override
    public Mono<CreditCardDTO> execute(CreditCardDTO creditCardDTO) {
        return userService.getLoggedInUser()
                .map(user -> user.addNewCreditCard(
                        creditCardDTO.title(),
                        creditCardDTO.description(),
                        creditCardDTO.color(),
                        creditCardDTO.number(),
                        creditCardDTO.invoiceClosingDay()
                ))
                .flatMap(creditCardRepository::save)
                .map(creditCardMapper::toDTO);
    }
}
