package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.BudgetTemplate;
import com.victor.financeapp.backend.domain.model.budget.BudgetTemplateCategory;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
interface BudgetTemplateEntityMapper {

    @Mapping(target = "categories", ignore = true)
    BudgetTemplate toDomain(BudgetTemplateEntity entity);

    BudgetTemplateEntity toEntity(BudgetTemplate template);

    BudgetTemplateCategory toDomain(BudgetTemplateCategoryEntity entity);

    @Mapping(target = "templateId", ignore = true)
    BudgetTemplateCategoryEntity toEntity(BudgetTemplateCategory category);
}
