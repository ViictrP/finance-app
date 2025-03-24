package com.victor.financeapp.backend.infrastructure.persistence.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_property")
@Getter
@Setter
public class UserPropertyEntity {
    private String propertyName;
    private String propertyValue;
    private Long userId;
}
