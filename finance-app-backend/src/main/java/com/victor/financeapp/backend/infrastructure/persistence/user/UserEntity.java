package com.victor.financeapp.backend.infrastructure.persistence.user;

import com.victor.financeapp.backend.infrastructure.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("finance_app_user")
@Getter
@Setter
class UserEntity extends Entity<Long> {
    private String name;
    private String lastname;
    private String email;
    private BigDecimal salary;
    private Boolean active;
}
