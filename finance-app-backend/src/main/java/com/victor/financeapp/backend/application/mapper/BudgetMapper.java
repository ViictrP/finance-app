package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.BudgetDTO;
import com.victor.financeapp.backend.domain.model.budget.Budget;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface BudgetMapper {

    @Mapping(target = "userId", ignore = true)
    Budget toDomain(BudgetDTO dto);

    BudgetDTO toDto(Budget budget);
}