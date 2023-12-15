package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.EquipmentRepository;
import com.isa.med_equipment.repository.EquipmentSpecifications;
import com.isa.med_equipment.service.CompanyService;
import com.isa.med_equipment.service.EquipmentService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CompanyService companyService;
    private final Mapper mapper;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, CompanyService companyService, Mapper mapper) {
        super();
        this.equipmentRepository = equipmentRepository;
        this.companyService = companyService;
        this.mapper = mapper;
    }
    @Override
    public List<Equipment> findAll()  {
        return equipmentRepository.findAll();
    }

    @Override
    public Page<EquipmentDto> findAllByCompanyIdPaged(String name, String type, Float rating, Long id, Pageable pageable) {
        List<EquipmentDto> eq;
        Specification<Equipment> spec = Specification.where(StringUtils.isBlank(name) ? null : EquipmentSpecifications.nameLike(name))
                .and(StringUtils.isBlank(type) ? null : EquipmentSpecifications.typeEquals(type))
                .and(rating == null ? null : EquipmentSpecifications.ratingGreaterThanOrEqual(rating));

        Page<Equipment> equipment = equipmentRepository.findAll(spec, PageRequest.of(0, Integer.MAX_VALUE));

        eq = companyService.findEquipmentByCompany(id);
        List<Equipment> filteredEquipment = equipment.getContent()
                .stream()
                .filter(e -> eq.stream().anyMatch(eqItem -> eqItem.getId().equals(e.getId())))
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Equipment> pageList;

        if (filteredEquipment.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, filteredEquipment.size());
            pageList = filteredEquipment.subList(startItem, toIndex);
        }

        equipment = new PageImpl<>(pageList, pageable, filteredEquipment.size());
        return mapper.mapPage(equipment, EquipmentDto.class);
    }

    @Override
    public Optional<Equipment> findById(Long id)  {
        return equipmentRepository.findById(id);
    }

    @Override
    public Page<EquipmentDto> findAllPaged(String name, String type, Float rating, Pageable pageable) {
        Specification<Equipment> spec = Specification.where(StringUtils.isBlank(name) ? null : EquipmentSpecifications.nameLike(name))
                .and(StringUtils.isBlank(type) ? null : EquipmentSpecifications.typeEquals(type))
                .and(rating == null ? null : EquipmentSpecifications.ratingGreaterThanOrEqual(rating));
        Page<Equipment> equipment = equipmentRepository.findAll(spec, pageable);
        return mapper.mapPage(equipment, EquipmentDto.class);
    }

    @Override
    @Transactional
    public EquipmentDto add(EquipmentDto equipmentDto) {
        Equipment equipment = new Equipment();

        equipment.setName(equipmentDto.getName());
        equipment.setDescription(equipmentDto.getDescription());
        equipment.setType(equipmentDto.getType());
        equipment.setRating(equipmentDto.getRating());
        equipment.setPrice(equipmentDto.getPrice());

        Equipment newEquipment = equipmentRepository.save(equipment);

        return mapper.map(newEquipment, EquipmentDto.class);
    }

    @Override
    @Transactional
    public EquipmentDto update(Long id, EquipmentDto equipmentDto) {

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);

        if (optionalEquipment.isPresent()) {

            Equipment equipment = optionalEquipment.get();

            equipment.setName(equipmentDto.getName());
            equipment.setDescription(equipmentDto.getDescription());
            equipment.setType(equipmentDto.getType());
            equipment.setPrice(equipmentDto.getPrice());

            Equipment updatedEquipment = equipmentRepository.save(equipment);
            return mapper.map(updatedEquipment, EquipmentDto.class);
        } else {
            throw new EntityNotFoundException("Equipment not found with id: " + id);
        }
    }
}
