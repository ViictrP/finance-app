package com.victor.financeapp.backend.domain.model.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
