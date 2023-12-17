package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #reservationDto.userId == authentication.principal.id)")
    public ResponseEntity<ReservationDto> makeReservation(@RequestBody ReservationDto reservationDto) {
        ReservationDto result = reservationService.reserve(reservationDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/timeslot/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<UserDto> getByTimeSlotId(@PathVariable Long id) {
        UserDto result = reservationService.getByTimeSlotId(id);
        return ResponseEntity.ok(result);
    }
}