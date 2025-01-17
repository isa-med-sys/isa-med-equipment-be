package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.*;
import com.isa.med_equipment.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    Page<CompanyDto> findAll(String name, String city, Float rating, Pageable pageable);
    List<Company> findAllTemp(); //temp
    CompanyDto findById(Long id);
    List<EquipmentDto> findEquipmentByCompany(Long id);
    List<EquipmentDto> findAvailableEquipmentByCompany(Long id);
    List<CompanyAdminDto> findAllAdmins(Long id);
    List<Long> findAllAdminIds(Long id);
    List<Company> findAllByEquipment(Long id);
    Company add(CompanyRegistrationDto companyRegistrationDto);
    Company update(Long id, CompanyDto companyDto);
    CompanyDto updateEquipment(Long id, List<EquipmentDto> equipmentDto);
}
