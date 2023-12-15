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
    public Long id;
    public CompanyAdminDto admin;
    public LocalDateTime start;
    public Duration duration = Duration.ofMinutes(30);
    private Boolean isFree;
}