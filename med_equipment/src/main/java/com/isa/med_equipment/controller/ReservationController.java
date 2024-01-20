package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
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

    @GetMapping("/timeslot/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<UserDto> getByTimeSlotId(@PathVariable Long id) {
        UserDto result = reservationService.getByTimeSlotId(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/equipment-update/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<Boolean> canUpdateEquipment(@PathVariable Long id, @RequestBody EquipmentDto equipmentDto) {
        Boolean result = reservationService.canUpdateEquipment(id, equipmentDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/equipment-delete/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<Boolean> canDeleteEquipment(@PathVariable Long id, @RequestParam Long equipmentId) {
        Boolean result = reservationService.canDeleteEquipment(id, equipmentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cancel-reservation")
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #reservationDto.userId == authentication.principal.id)")
    public ResponseEntity<ReservationDto> cancelReservation(@RequestBody ReservationDto reservationDto) {
        ReservationDto result = reservationService.cancelReservation(reservationDto);
        return ResponseEntity.ok(result);
    }
}