package com.victor.financeapp.backend.application.mapper;

import com.victor.financeapp.backend.application.dto.UserBalanceDTO;
import com.victor.financeapp.backend.domain.model.user.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CreditCardMapper.class, TransactionMapper.class})
public interface BalanceMapper {
    UserBalanceDTO toDTO(Balance balance);
}
