package com.isa.med_equipment.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservationDto {
    private Long userId;
    private Long companyId;
    private List<Long> equipmentIds;
    private Long timeSlotId;
    private Boolean isPickedUp = false;
    private Boolean isCancelled = false;
    private byte[] qrCode;
}