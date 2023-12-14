package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.CalendarDto;
import com.isa.med_equipment.dto.TimeSlotDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CalendarService {

    CalendarDto findByCompany(Long companyId);
    List<TimeSlotDto> findFreePredefinedTimeSlotsByCompany(Long companyId);
    TimeSlotDto createTimeSlot(TimeSlotDto timeSlotDto);
}
