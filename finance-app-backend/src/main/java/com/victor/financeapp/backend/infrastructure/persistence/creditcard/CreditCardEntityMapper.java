package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface CreditCardEntityMapper {

    @Mapping(target = "color", source = "backgroundColor")
    CreditCard toDomain(CreditCardEntity entity);
}
