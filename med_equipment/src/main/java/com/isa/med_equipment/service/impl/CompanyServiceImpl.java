package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.*;
import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.CompanySpecifications;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.CompanyService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, Mapper mapper) {
        super();
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<CompanyDto> findAll(String name, String city, Float rating, Pageable pageable) {
        Specification<Company> spec = Specification.where(StringUtils.isBlank(name) ? null : CompanySpecifications.nameLike(name))
                .and(StringUtils.isBlank(city) ? null : CompanySpecifications.cityLike(city))
                .and(rating == null ? null : CompanySpecifications.ratingGreaterThanOrEqual(rating));

        Page<Company> companies = companyRepository.findAll(spec, pageable);
        return mapper.mapPage(companies, CompanyDto.class);
    }

    @Override
    public List<Company> findAllTemp() {
        return companyRepository.findAll();
    }

    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));
        return mapper.map(company, CompanyDto.class);
    }

    public Company findCompany(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));
    }

    @Override
    public List<Company> findAllByEquipment(Long id) {
        List<Company> companies = companyRepository.findAll();
        List<Company> filteredCompanies = new ArrayList<>();

        for (Company company : companies) {
            boolean hasEquipmentWithId = false;
            for (Equipment equipment : company.getEquipment()) {
                if (equipment.getId().equals(id)) {
                    hasEquipmentWithId = true;
                    break;
                }
            }
            if (hasEquipmentWithId) {
                filteredCompanies.add(company);
            }
        }
        return filteredCompanies;
    }

    @Override
    public List<CompanyAdmin> findAllAdmins(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        return company.getAdmins();
    }

    @Override
    @Transactional
    public Company add(CompanyDto companyDto) {
        Company company = new Company();

        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setRating(companyDto.getRating());

        Address address = new Address();

        address.setCity(companyDto.getAddress().getCity());
        address.setCountry(companyDto.getAddress().getCountry());
        address.setStreet(companyDto.getAddress().getStreet());
        address.setStreetNumber(companyDto.getAddress().getStreetNumber());

        company.setAddress(address);

        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public Company update(Long id, CompanyDto companyDto) {

        Optional<Company> optionalCompany = companyRepository.findById(id);

        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            company.setName(companyDto.getName());
            company.setDescription(companyDto.getDescription());
            company.setRating(companyDto.getRating());

            company.getAddress().updateAddress(
                    companyDto.getAddress().getStreet(),
                    companyDto.getAddress().getStreetNumber(),
                    companyDto.getAddress().getCity(),
                    companyDto.getAddress().getCountry()
            );

            return companyRepository.save(company);
        } else {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
    }
}
