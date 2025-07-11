package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.BudgetTemplateCategory;
import com.victor.financeapp.backend.domain.model.common.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface BudgetTemplateCategoryEntityMapper {

    @Mapping(source = "category", target = "name")
    BudgetTemplateCategory toDomain(BudgetTemplateCategoryEntity entity);

    @Mapping(source = "name", target = "category")
    @Mapping(target = "templateId", ignore = true)
    BudgetTemplateCategoryEntity toEntity(BudgetTemplateCategory category);

    default String fromCategory(Category category) {
        return category == null ? null : category.name();
    }

    default Category toCategory(String category) {
        return category == null ? null : Category.valueOf(category);
    }
}
