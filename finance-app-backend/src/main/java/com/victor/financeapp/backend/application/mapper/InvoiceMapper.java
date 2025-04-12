package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.InvoiceDTO;
import com.victor.financeapp.backend.domain.model.creditcard.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TransactionMapper.class)
public interface InvoiceMapper {

    InvoiceDTO toDTO(Invoice invoice);

    default List<InvoiceDTO> toDTO(List<Invoice> invoices) {
        return invoices.stream().map(this::toDTO).toList();
    }
}
