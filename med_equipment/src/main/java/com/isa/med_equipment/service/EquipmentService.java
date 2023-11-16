package com.isa.med_equipment.service;

import com.isa.med_equipment.model.Equipment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EquipmentService {
    List<Equipment> findAll();
    Optional<Equipment> findById(Long id);
}