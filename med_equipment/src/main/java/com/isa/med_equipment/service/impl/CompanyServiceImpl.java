package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyAdminDto;
import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.CompanySpecifications;
import com.isa.med_equipment.service.CompanyService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<EquipmentDto> findEquipmentByCompany(Long id) {
        Company company =  companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        List<EquipmentDto> equipmentDtos = new ArrayList<>();

        for (Map.Entry<Equipment, Integer> entry : company.getEquipment().entrySet()) {
            Equipment equipment = entry.getKey();
            Integer quantity = entry.getValue();
            EquipmentDto equipmentDto = mapper.map(equipment, EquipmentDto.class);
            equipmentDto.setQuantity(quantity);
            equipmentDtos.add(equipmentDto);
        }

        return equipmentDtos;
    }

    @Override
    public List<EquipmentDto> findAvailableEquipmentByCompany(Long id) {
        Company company =  companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        List<Equipment> availableEquipment = company.getEquipment().entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return mapper.mapList(availableEquipment, EquipmentDto.class);
    }

    @Override
    public List<Company> findAllByEquipment(Long id) {
        List<Company> companies = companyRepository.findAll();
        List<Company> filteredCompanies = new ArrayList<>();

        for (Company company : companies) {
            boolean hasEquipmentWithId = false;
            for (Equipment equipment : company.getEquipment().keySet()) {
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
    public List<CompanyAdminDto> findAllAdmins(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        List<CompanyAdmin> companyAdmins = company.getAdmins();

        return mapper.mapList(companyAdmins, CompanyAdminDto.class);
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

    public CompanyDto updateEquipment(Long id, List<EquipmentDto> equipmentDto) {

        Optional<Company> optionalCompany = companyRepository.findById(id);

        if (optionalCompany.isPresent()) {

            Company company = optionalCompany.get();

            Map<Equipment, Integer> updatedEquipment = new HashMap<>();

            for (EquipmentDto eq : equipmentDto) {
                updatedEquipment.put(mapper.map(eq, Equipment.class), eq.getQuantity());
            }

            company.setEquipment(updatedEquipment);

            company = companyRepository.save(company);
            return mapper.map(company, CompanyDto.class);
        } else {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
    }
}
