package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.domain.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO toDTO(Transaction transaction);

    default List<TransactionDTO> toDTO(List<Transaction> transactions) {
        return transactions.stream().map(this::toDTO).toList();
    }
}
