package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Page<ReservationDto>> getReservationsByUser(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam Long userId) {
        Page<ReservationDto> result = reservationService.findAllByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #reservationDto.userId == authentication.principal.id)")
    public ResponseEntity<ReservationDto> makeReservation(@RequestBody ReservationDto reservationDto) {
            ReservationDto result = reservationService.reserve(reservationDto);
            return ResponseEntity.ok(result);
    }
}