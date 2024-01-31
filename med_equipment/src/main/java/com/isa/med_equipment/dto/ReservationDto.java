package com.isa.med_equipment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long companyId;
    private List<Long> equipmentIds;
    private Long timeSlotId;
    private Double price;
    private Boolean isPickedUp = false;
    private Boolean isCancelled = false;
    private byte[] qrCode;
    private EquipmentDto equipment;
    private String companyName;
    private LocalDateTime start;
}