package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.repository.*;
import com.isa.med_equipment.service.EquipmentService;
import com.isa.med_equipment.util.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    //private final Mapper mapper;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, CompanyRepository companyRepository, UserRepository userRepository) {//mm
        super();
        this.equipmentRepository = equipmentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        //this.mapper = mapper;
    }
    @Override
    public List<Equipment> findAll()  {
        return equipmentRepository.findAll();
    }

    @Override
    public List<Equipment> findAllByCompanyId(Long id) { //u
        Company company = companyRepository.findById(id).orElse(new Company());
        return company.getEquipment().stream().toList();
    }

    @Override
    public List<Equipment> search(String name, String type, Float rating, String userRole, Long id) {
        List<Equipment> equipment = equipmentRepository.findAll();
        if(userRole.equals("COMPANY_ADMIN")) {
//            Optional<User> user = userRepository.findById(id);
//            Optional<CompanyAdmin> companyAdmin = user.filter(u -> u instanceof CompanyAdmin).map(u -> (CompanyAdmin) u);
//            CompanyAdmin admin = companyAdmin.orElseThrow(() -> new RuntimeException("User is not a CompanyAdmin"));
//            Company company = admin.getCompany();
//            equipment = findAllByCompanyId(company.getId());
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

//    @Override
//    public Page<EquipmentDto> findAllPaged(String name, String type, Float rating, Pageable pageable) {
//        Specification<Equipment> spec = Specification.where(StringUtils.isBlank(name) ? null : EquipmentSpecifications.nameLike(name))
//                .and(StringUtils.isBlank(type) ? null : EquipmentSpecifications.typeLike(type))
//                .and(rating == null ? null : EquipmentSpecifications.ratingGreaterThanOrEqual(rating));
//
//        Page<Equipment> equipment = equipmentRepository.findAll(spec, pageable);
//        return mapper.mapPage(equipment, EquipmentDto.class);
//    }
}
