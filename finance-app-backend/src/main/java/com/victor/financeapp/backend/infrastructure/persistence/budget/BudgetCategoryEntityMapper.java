package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.BudgetCategory;
import com.victor.financeapp.backend.domain.model.common.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface BudgetCategoryEntityMapper {

    @Mapping(source = "category", target = "name")
    BudgetCategory toDomain(BudgetCategoryEntity entity);

    @Mapping(source = "name", target = "category")
    @Mapping(target = "budgetId", ignore = true)
    BudgetCategoryEntity toEntity(BudgetCategory category);

    default String fromCategory(Category category) {
        return category == null ? null : category.name();
    }

    default Category toCategory(String category) {
        return category == null ? null : Category.valueOf(category);
    }
}
