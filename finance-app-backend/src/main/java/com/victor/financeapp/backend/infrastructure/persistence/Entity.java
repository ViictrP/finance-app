package com.victor.financeapp.backend.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Entity<ID extends Serializable> {
    private ID id;
    private LocalDateTime createdAt;
    private LocalDateTime modificatedAt;
    private Boolean deleted = false;
    private LocalDateTime deletedAt;

    public boolean isNew() {
        return this.id == null;
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
