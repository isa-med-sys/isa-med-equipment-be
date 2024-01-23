package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/past")
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Page<ReservationDto>> getPastReservationsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam Long userId,
            @RequestParam(name = "sort", required = false, defaultValue = "start") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection) {

        if(sortField.equalsIgnoreCase("start")) {
            sortField = "timeSlot.start";
        }
        else if (sortField.equalsIgnoreCase("companyName")) {
            sortField = "timeSlot.admin.company.name";
        }

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<ReservationDto> result = reservationService.findPastByUser(userId, pageRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/upcoming")
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Page<ReservationDto>> getUpcomingReservationsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam Long userId,
            @RequestParam(name = "sort", required = false, defaultValue = "start") String sortField,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String sortDirection) {

        if(sortField.equalsIgnoreCase("start")) {
            sortField = "timeSlot.start";
        }
        else if (sortField.equalsIgnoreCase("companyName")) {
            sortField = "timeSlot.admin.company.name";
        }

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<ReservationDto> result = reservationService.findUpcomingByUser(userId, pageRequest);
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

    @PostMapping("/cancel")
    @PreAuthorize("(hasRole('ROLE_REGISTERED_USER') and #reservationDto.userId == authentication.principal.id)")
    public ResponseEntity<ReservationDto> cancelReservation(@RequestBody ReservationDto reservationDto) {
        ReservationDto result = reservationService.cancelReservation(reservationDto);
        return ResponseEntity.ok(result);
    }
}