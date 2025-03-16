package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface CreditCardEntityMapper {

    CreditCard toDomain(CreditCardEntity entity);
}
