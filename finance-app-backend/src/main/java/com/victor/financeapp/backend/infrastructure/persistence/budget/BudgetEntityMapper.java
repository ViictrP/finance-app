package com.victor.financeapp.backend.infrastructure.persistence.budget;

import com.victor.financeapp.backend.domain.model.budget.Budget;
import com.victor.financeapp.backend.domain.model.budget.BudgetCategory;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
interface BudgetEntityMapper {

    @Mapping(target = "month", expression = "java(toYearMonth(entity.getMonth()))")
    @Mapping(target = "categories", ignore = true)
    Budget toDomain(BudgetEntity entity);

    @Mapping(target = "month", expression = "java(fromYearMonth(budget.getMonth()))")
    BudgetEntity toEntity(Budget budget);

    BudgetCategory toDomain(BudgetCategoryEntity entity);

    @Mapping(target = "budgetId", ignore = true)
    BudgetCategoryEntity toEntity(BudgetCategory category);

    default YearMonth toYearMonth(String yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    default String fromYearMonth(YearMonth yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}