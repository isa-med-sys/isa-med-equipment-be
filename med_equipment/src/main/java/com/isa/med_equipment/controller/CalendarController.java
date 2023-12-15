package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CalendarDto;
import com.isa.med_equipment.dto.TimeSlotDto;
import com.isa.med_equipment.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<CalendarDto> findByCompany(@RequestParam Long companyId){
        CalendarDto result = calendarService.findByCompany(companyId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/time-slots/free-predefined")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN', 'ROLE_REGISTERED_USER')")
    public ResponseEntity<List<TimeSlotDto>> findFreePredefinedTimeSlotsByCompany(@RequestParam Long companyId){
        List<TimeSlotDto> result = calendarService.findFreePredefinedTimeSlotsByCompany(companyId);
        return ResponseEntity.ok(result);
    }
}