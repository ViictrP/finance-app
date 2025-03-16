package com.victor.financeapp.backend.infrastructure.persistence.user;

import com.victor.financeapp.backend.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserEntityMapper {

    User toDomain(UserEntity entity);
}
