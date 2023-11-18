package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.CompanySpecifications;
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

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final Mapper mapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, Mapper mapper) {
        super();
        this.companyRepository = companyRepository;
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
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));
        return mapper.map(company, CompanyDto.class);
    }

    @Override
    public Company add(CompanyDto companyDto) {
        Company company = new Company();

        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setRating(companyDto.getRating());
        company.setEquipment(companyDto.getEquipment());

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
            company.setEquipment(companyDto.getEquipment());
            company.setAdmins(companyDto.getCompanyAdmins());

            Address address = new Address();

            address.setCity(companyDto.getAddress().getCity());
            address.setCountry(companyDto.getAddress().getCountry());
            address.setStreet(companyDto.getAddress().getStreet());
            address.setStreetNumber(companyDto.getAddress().getStreetNumber());

            company.setAddress(address);

            return companyRepository.save(company);
        } else {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
    }
}
