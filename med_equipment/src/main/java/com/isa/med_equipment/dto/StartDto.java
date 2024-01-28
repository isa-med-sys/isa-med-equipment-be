package com.isa.med_equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartDto {

    private Long userId;
    private Long companyId;
    private Float longitudeStart;
    private Float latitudeStart;
    private Float longitudeEnd;
    private Float latitudeEnd;
}
