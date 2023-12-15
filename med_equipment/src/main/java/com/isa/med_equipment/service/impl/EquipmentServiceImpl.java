package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.EquipmentRepository;
import com.isa.med_equipment.repository.EquipmentSpecifications;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.EquipmentService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final Mapper mapper;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, CompanyRepository companyRepository, UserRepository userRepository, Mapper mapper) {
        super();
        this.equipmentRepository = equipmentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    @Override
    public List<Equipment> findAll()  {
        return equipmentRepository.findAll();
    }

    @Override
    public List<Equipment> findAllByCompanyId(Long id) { //u
        Company company = companyRepository.findById(id).orElse(new Company());
        return company.getEquipment().keySet().stream().toList();
    }

    @Override
    public List<Equipment> search(String name, String type, Float rating, String userRole, Long id) {
        List<Equipment> equipment = equipmentRepository.findAll();
        if(userRole.equals("COMPANY_ADMIN")) {
            equipment = userRepository.findById(id)
                    .filter(u -> u instanceof CompanyAdmin)
                    .map(u -> (CompanyAdmin) u)
                    .map(CompanyAdmin::getCompany)
                    .map(company -> findAllByCompanyId(company.getId()))
                    .orElseThrow(() -> new RuntimeException("User is not a CompanyAdmin"));
        }
        if (name != null && !name.isEmpty()) {
            equipment = equipment.stream()
                    .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            equipment = equipment.stream()
                    .filter(e -> type.equals("") || e.getType().toString().equals(type))
                    .collect(Collectors.toList());
        }
        if (rating != null) {
            equipment = equipment.stream()
                    .filter(e -> e.getRating() != null && e.getRating() >= rating)
                    .collect(Collectors.toList());
        }
        return equipment;
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
        return mapper.mapPage(equipment, EquipmentDto.class); //valid
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
