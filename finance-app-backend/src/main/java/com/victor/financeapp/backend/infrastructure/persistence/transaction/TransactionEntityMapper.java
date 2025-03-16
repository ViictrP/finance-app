package com.victor.financeapp.backend.infrastructure.persistence.transaction;

import com.victor.financeapp.backend.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TransactionEntityMapper {

    Transaction toDomain(TransactionEntity transaction);
}
