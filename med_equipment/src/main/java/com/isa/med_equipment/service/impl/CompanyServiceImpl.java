package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        super();
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company add(CompanyDto companyDto) {
        Company company = new Company();

        company.setName(companyDto.getName());
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
    public Company update(Long id, CompanyDto companyDto) {

        Optional<Company> optionalCompany = companyRepository.findById(id);

        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            company.setName(companyDto.getName());
            company.setRating(companyDto.getRating());
            company.setEquipment(companyDto.getEquipment());

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
