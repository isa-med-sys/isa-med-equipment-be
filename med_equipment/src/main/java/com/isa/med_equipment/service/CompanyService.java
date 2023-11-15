package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CompanyService {
    List<Company> findAll();
    Optional<Company> findById(Long id);
    Company add(CompanyDto companyDto);
    Company update(Long id, CompanyDto companyDto);
}
