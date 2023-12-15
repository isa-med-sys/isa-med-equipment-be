package com.isa.med_equipment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlotDto {
    private Long id;
    private CompanyAdminDto admin;
    private Long companyAdminId;
    private LocalDateTime start;
    private Duration duration = Duration.ofMinutes(30);
    private Boolean isFree;
}