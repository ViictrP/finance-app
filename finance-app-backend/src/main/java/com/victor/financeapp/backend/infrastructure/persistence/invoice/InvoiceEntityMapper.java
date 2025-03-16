package com.victor.financeapp.backend.infrastructure.persistence.invoice;

import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import org.mapstruct.Mapper;

import java.time.Month;
import java.time.YearMonth;

@Mapper(componentModel = "spring")
interface InvoiceEntityMapper {

    default Invoice toDomain(InvoiceEntity entity) {
        var yearMonth = YearMonth.of(entity.getYear(), Month.valueOf(entity.getMonth()));
        return Invoice.create(entity.getId(), entity.getCreditCardId(), yearMonth);
    }
}
