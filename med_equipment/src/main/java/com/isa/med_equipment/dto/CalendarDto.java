package com.isa.med_equipment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CalendarDto {
    public Long id;
    public CompanyDto company;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Boolean worksOnWeekends;
    private List<TimeSlotDto> timeSlots;
}
