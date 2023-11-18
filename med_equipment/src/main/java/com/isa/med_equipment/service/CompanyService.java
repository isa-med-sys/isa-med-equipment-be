package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CompanyService {
    Page<CompanyDto> findAll(String name, String city, Float rating, Pageable pageable);
    CompanyDto findById(Long id);
    Company add(CompanyDto companyDto);
    Company update(Long id, CompanyDto companyDto);
}
