package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record BudgetTemplateDTO(
        Long id,
        BigDecimal totalAmount,
        List<BudgetTemplateCategoryDTO> categories
) {
}
