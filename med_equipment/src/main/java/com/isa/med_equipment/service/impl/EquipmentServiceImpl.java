package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.EquipmentRepository;
import com.isa.med_equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        super();
        this.equipmentRepository = equipmentRepository;
    }
    @Override
    public List<Equipment> findAll()  {
        return equipmentRepository.findAll();
    }

    @Override
    public List<Equipment> search(String name, String type, Float rating) {
        List<Equipment> equipment = equipmentRepository.findAll();
        //filter
        return equipment;
    }

    @Override
    public Optional<Equipment> findById(Long id)  {
        return equipmentRepository.findById(id);
    }
}
