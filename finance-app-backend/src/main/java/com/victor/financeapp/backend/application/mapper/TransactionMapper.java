package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "creditCardId", source = "invoice.creditCardId")
    TransactionDTO toDTO(Transaction transaction);
    Transaction toDomain(TransactionDTO dto);

    default List<TransactionDTO> toDTO(List<Transaction> transactions) {
        return transactions.stream().map(this::toDTO).toList();
    }
}
