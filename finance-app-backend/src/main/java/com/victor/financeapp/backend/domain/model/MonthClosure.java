package com.victor.financeapp.backend.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
public class MonthClosure {
    private String month;
    private Integer year;
    private BigDecimal total;
    private BigDecimal available;
    private BigDecimal expenses;
    private Integer index;
    private BigDecimal finalUsdToBRL;

    @Setter
    private User user;
}
