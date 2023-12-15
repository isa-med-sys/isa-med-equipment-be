package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EquipmentService {
    List<Equipment> findAll();
    Page<EquipmentDto> findAllPaged(String name, String type, Float rating, Pageable pageable);
    Page<EquipmentDto> findAllByCompanyIdPaged(String name, String type, Float rating, Long id, Pageable pageable);
    Optional<Equipment> findById(Long id);
}