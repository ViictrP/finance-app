package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonthMapping {

    private static final Map<String, Month> MONTHS;

    static {
        MONTHS = new HashMap<>();
        MONTHS.put("JAN", Month.JANUARY);
        MONTHS.put("FEB", Month.FEBRUARY);
        MONTHS.put("MAR", Month.MARCH);
        MONTHS.put("APR", Month.APRIL);
        MONTHS.put("MAY", Month.MAY);
        MONTHS.put("JUN", Month.JUNE);
        MONTHS.put("JUL", Month.JULY);
        MONTHS.put("AUG", Month.AUGUST);
        MONTHS.put("SEP", Month.SEPTEMBER);
        MONTHS.put("OCT", Month.OCTOBER);
        MONTHS.put("NOV", Month.NOVEMBER);
        MONTHS.put("DEC", Month.DECEMBER);
    }

    public static Month getMonthFromAbbreviation(String abbreviation) {
        return MONTHS.get(abbreviation);
    }
}
