package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import com.victor.financeapp.backend.domain.model.common.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface TransactionEntityMapper {

    Transaction toDomain(TransactionEntity transaction);

    @Mapping(target = "invoiceId", source = "invoice.id")
    TransactionEntity toEntity(Transaction transaction);
}
