package com.isa.med_equipment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractNotificationDto {

    private Long userId;
    private LocalDateTime timestamp;
    private String message;

    public ContractNotificationDto(Long userId, String message) {
        this.userId = userId;
        this.message = message;
        timestamp = LocalDateTime.now();
    }
}
