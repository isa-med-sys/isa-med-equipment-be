package com.isa.med_equipment.dto;

import lombok.Data;

@Data
public class LocationDto {
    private Long userId;
    private Float longitude;
    private Float latitude;
}
