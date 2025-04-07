package com.victor.financeapp.backend.application.dto;

import java.math.BigDecimal;

public record MonthClosureDTO(
        String month,
        Integer year,
        BigDecimal total,
        BigDecimal available,
        BigDecimal expenses,
        Integer index,
        BigDecimal finalUsdToBRL
) {
}
