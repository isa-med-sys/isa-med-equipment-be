package com.isa.med_equipment.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Company admin is required")
    public Long companyAdminId;

    @NotEmpty(message = "Start date is required")
    public LocalDateTime start;

    public Duration duration = Duration.ofMinutes(30);
    private Boolean isFree;

    public TimeSlotDto(@NotEmpty(message = "Company admin is required") Long companyAdminId,
                       @NotEmpty(message = "Start date is required") LocalDateTime start,
                       Long id, Boolean isFree) {
        super();
        this.id = id;
        this.companyAdminId = companyAdminId;
        this.start = start;
        this.isFree = isFree;
    }
}