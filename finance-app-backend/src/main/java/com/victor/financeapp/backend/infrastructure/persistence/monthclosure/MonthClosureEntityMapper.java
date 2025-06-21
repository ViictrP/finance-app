package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface MonthClosureEntityMapper {
    MonthClosure toDomain(MonthClosureEntity entity);

    @Mapping(source = "domain.user.id", target = "userId")
    MonthClosureEntity toEntity(MonthClosure domain);
}
