package com.isa.med_equipment.dto;

import lombok.Data;

@Data
public class DeliveryStartDto {

    private Long userId;
    private long totalDurationInMinutes;
}