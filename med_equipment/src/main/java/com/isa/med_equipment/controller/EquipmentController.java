package com.isa.med_equipment.controller;

import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_COMPANY_ADMIN', 'ROLE_REGISTERED_USER')")
    public ResponseEntity<List<Equipment>> findAll() {
        return new ResponseEntity<>(equipmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_COMPANY_ADMIN', 'ROLE_REGISTERED_USER')") // ??
    public ResponseEntity<Optional<Equipment>> getById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        return equipment.isPresent() ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_COMPANY_ADMIN', 'ROLE_REGISTERED_USER')")
    public ResponseEntity<List<Equipment>> getByParams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Float rating,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String role) {
        return new ResponseEntity<>(equipmentService.search(name, type, rating, role, id), HttpStatus.OK);
    }

//    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_REGISTERED_USER')")
//    public ResponseEntity<Page<EquipmentDto>> getByParams(@RequestParam(defaultValue = "0") int page,
//                                                          @RequestParam(defaultValue = "5") int size,
//                                                          @RequestParam(name = "name", required = false) String name,
//                                                          @RequestParam(name = "type", required = false) String type,
//                                                          @RequestParam(name = "rating", required = false) Float rating) {
//        return new ResponseEntity<>(equipmentService.findAllPaged(name, type, rating, PageRequest.of(page, size)), HttpStatus.OK);
//    }
}
