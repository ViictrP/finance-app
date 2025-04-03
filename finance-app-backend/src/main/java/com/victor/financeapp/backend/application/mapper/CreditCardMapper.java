package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.CreditCardDTO;
import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = InvoiceMapper.class)
public interface CreditCardMapper {

    CreditCardDTO toDTO(CreditCard creditCard);

    default List<CreditCardDTO> toDTO(List<CreditCard> creditCards) {
        return creditCards.stream().map(this::toDTO).toList();
    }
}
