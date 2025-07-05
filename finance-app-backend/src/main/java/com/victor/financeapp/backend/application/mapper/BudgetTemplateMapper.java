package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.BudgetTemplateDTO;
import com.victor.financeapp.backend.domain.model.budget.BudgetTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetTemplateMapper {

    BudgetTemplate toDomain(BudgetTemplateDTO dto);
    BudgetTemplateDTO toDTO(BudgetTemplate domain);
}
