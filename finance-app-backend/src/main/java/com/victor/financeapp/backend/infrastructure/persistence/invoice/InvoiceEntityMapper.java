package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
interface InvoiceEntityMapper {

    default Invoice toDomain(InvoiceEntity entity) {
        var yearMonth = YearMonth.of(entity.getYear(), MonthMapping.getMonthFromAbbreviation(entity.getMonth()));
        return Invoice.create(entity.getId(), entity.getCreditCardId(), yearMonth);
    }

    @Mapping(target = "year", source = "yearMonth.year")
    @Mapping(target = "month", source = "yearMonth", qualifiedByName = "toMonthAbbreviation")
    InvoiceEntity toEntity(Invoice invoice);

    @Named("toMonthAbbreviation")
    static String toMonthAbbreviation(YearMonth ym) {
        return ym.atDay(1)
                .format(DateTimeFormatter.ofPattern("MMM"))
                .toUpperCase();
    }
}
