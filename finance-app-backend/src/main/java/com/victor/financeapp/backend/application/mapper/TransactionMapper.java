package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.TransactionDTO;
import com.victor.financeapp.backend.domain.model.common.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "date", expression = "java(toOffsetDateTime(transaction.getDate()))")
    TransactionDTO toDTO(Transaction transaction);

    default List<TransactionDTO> toDTO(List<Transaction> transactions) {
        return transactions.stream().map(this::toDTO).toList();
    }

    default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }
}
