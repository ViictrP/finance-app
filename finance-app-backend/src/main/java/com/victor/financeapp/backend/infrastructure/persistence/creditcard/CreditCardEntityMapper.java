package com.victor.financeapp.backend.infrastructure.persistence.creditcard;

import com.victor.financeapp.backend.domain.model.creditcard.CreditCard;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
interface CreditCardEntityMapper {

    @Mapping(target = "color", source = "backgroundColor")
    CreditCard toDomain(CreditCardEntity entity);

    @Mapping(target = "backgroundColor", source = "color")
    CreditCardEntity toEntity(CreditCard creditCard);
}
