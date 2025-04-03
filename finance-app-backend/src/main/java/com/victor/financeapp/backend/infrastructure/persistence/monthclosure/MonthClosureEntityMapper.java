package com.victor.financeapp.backend.infrastructure.persistence.monthclosure;

import com.victor.financeapp.backend.domain.model.user.MonthClosure;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonthClosureEntityMapper {

    MonthClosure toDomain(MonthClosureEntity entity);
}
