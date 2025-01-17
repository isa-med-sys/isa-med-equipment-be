package com.isa.med_equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDto {

    private Long id;
    private Long userId;
    private Long companyId;
    private LocalDate startDate;
    private Map<Long, Integer> equipmentQuantities;
    private Boolean isActive;

    private Map<String, Integer> namedEquipmentQuantities;
    private String hospitalName;
    private Boolean canStart;
    private Boolean delete = false;
}
