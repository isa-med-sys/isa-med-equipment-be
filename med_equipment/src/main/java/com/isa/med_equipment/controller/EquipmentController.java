package com.isa.med_equipment.controller;

import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping()
    public ResponseEntity<List<Equipment>> findAll() {
        return new ResponseEntity<>(equipmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Equipment>> getById(@PathVariable Long id) {
        Optional<Equipment> equipment = equipmentService.findById(id);
        return equipment.isPresent() ? ResponseEntity.ok(equipment) : ResponseEntity.notFound().build();
    }
}
