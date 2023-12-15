package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_REGISTERED_USER')")
    public ResponseEntity<Page<EquipmentDto>> getByParams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "rating", required = false) Float rating) {
        Page<EquipmentDto> result = equipmentService.findAllPaged(name, type, rating,  PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_COMPANY_ADMIN', 'ROLE_REGISTERED_USER')")
    public ResponseEntity<Optional<Equipment>> getById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        return equipment.isPresent() ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<Page<EquipmentDto>> getByParamsCa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Float rating,
            @RequestParam(required = false) Long id) {
        return new ResponseEntity<>(equipmentService.findAllByCompanyIdPaged(name, type, rating, id, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> add(@RequestBody EquipmentDto equipmentDto) {
        try {
            EquipmentDto newEquipment = equipmentService.add(equipmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEquipment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipmentDto equipmentDto) {
        try {
            EquipmentDto updatedEquipment = equipmentService.update(id, equipmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedEquipment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
