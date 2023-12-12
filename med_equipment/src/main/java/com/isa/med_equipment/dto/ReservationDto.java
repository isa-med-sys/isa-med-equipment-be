package com.isa.med_equipment.dto;

import lombok.Data;

@Data
public class ReservationDto {
    private Long userId;
    private Long equipmentId;
    private Long timeSlotId;
}