package com.victor.financeapp.backend.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Entity<ID extends Serializable> {
    @Id
    private ID id;
    private LocalDateTime createdAt;
    private LocalDateTime modificatedAt;
    private Boolean deleted = false;
    private LocalDateTime deleteDate;

    public boolean isNew() {
        return this.id == null;
    }

    public void delete() {
        this.deleted = true;
        this.deleteDate = LocalDateTime.now();
    }
}
