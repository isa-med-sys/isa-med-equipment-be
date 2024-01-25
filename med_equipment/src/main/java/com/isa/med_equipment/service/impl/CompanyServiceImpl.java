package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.*;
import com.isa.med_equipment.model.*;
import com.isa.med_equipment.repository.*;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CalendarRepository calendarRepository;
    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;
    private final Mapper mapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository,
                              CalendarRepository calendarRepository,
                              EquipmentRepository equipmentRepository,
                              ReservationRepository reservationRepository,
                              Mapper mapper) {
        super();
        this.companyRepository = companyRepository;
        this.equipmentRepository = equipmentRepository;
        this.calendarRepository = calendarRepository;
        this.reservationRepository = reservationRepository;
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

        Calendar calendar = calendarRepository.findByCompany_Id(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        CompanyDto result = mapper.map(company, CompanyDto.class);

        result.setWorkStartTime(calendar.getWorkStartTime());
        result.setWorkEndTime(calendar.getWorkEndTime());
        result.setWorksOnWeekends(calendar.getWorksOnWeekends());

        return result;
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
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        List<Equipment> availableEquipment = company.getEquipment().keySet()
                .stream()
                .filter(equip -> isEquipmentAvailable(company, equip))
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
    public List<Long> findAllAdminIds(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with ID %d not found!", id)));

        List<CompanyAdmin> companyAdmins = company.getAdmins();

        return companyAdmins.stream()
                .map(CompanyAdmin::getId)
                .toList();
    }

    @Override
    public Company add(CompanyRegistrationDto companyRegistrationDto) {
        Company company = new Company();

        company.setName(companyRegistrationDto.getName());
        company.setDescription(companyRegistrationDto.getDescription());
        company.setRating(3.0f); //companyRegistrationDto.getRating()

        Address address = new Address();

        address.setCity(companyRegistrationDto.getAddress().getCity());
        address.setCountry(companyRegistrationDto.getAddress().getCountry());
        address.setStreet(companyRegistrationDto.getAddress().getStreet());
        address.setStreetNumber(companyRegistrationDto.getAddress().getStreetNumber());
        address.setLongitude(companyRegistrationDto.getAddress().getLongitude());
        address.setLatitude(companyRegistrationDto.getAddress().getLatitude());

        company.setAddress(address);

        Company newCompany = companyRepository.save(company); //

        Calendar calendar = new Calendar();
        calendar.setWorksOnWeekends(true); //companyRegistrationDto.getWorksOnWeekends()
        calendar.setWorkStartTime(companyRegistrationDto.getWorkStartTime());
        calendar.setWorkEndTime(companyRegistrationDto.getWorkEndTime());
        calendar.setCompany(newCompany);

        Calendar newCalendar = calendarRepository.save(calendar); //

        return newCompany;
    }

    @Override
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
                    companyDto.getAddress().getCountry(),
                    companyDto.getAddress().getLongitude(),
                    companyDto.getAddress().getLatitude()
            );

            return companyRepository.save(company);
        } else {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
    }

    @Override
    public CompanyDto updateEquipment(Long id, List<EquipmentDto> equipmentDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found."));

        List<EquipmentDto> updatedEquipment = new ArrayList<>();
        for (EquipmentDto equip : equipmentDto) {
            if (equip.getRemove() != null && equip.getRemove()) {
                canDeleteEquipment(company.getId(), equip.getId());
            } else {
                canUpdateEquipment(company.getId(), equip);
                updatedEquipment.add(equip);
            }
        }

        company.getEquipment().clear();

        for (EquipmentDto eq : updatedEquipment) {
            Equipment equipment = equipmentRepository.findById(eq.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found."));

            company.getEquipment().put(equipment, eq.getQuantity());
        }

        Company updatedCompany = companyRepository.save(company);
        return mapper.map(updatedCompany, CompanyDto.class);
    }

    private void canDeleteEquipment(Long companyId, Long equipmentId) {
       Equipment equipment = equipmentRepository.findWithLockingById(equipmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found."));

        int reservedQuantity = reservationRepository.getTotalReservedQuantity(equipment, companyId);

        if (reservedQuantity > 0) {
            throw new IllegalStateException("Can't delete equipment.");
        }
    }

    private void canUpdateEquipment(Long companyId, EquipmentDto equipmentDto) {
        int newQuantity = equipmentDto.getQuantity();

        Equipment equipment = equipmentRepository.findWithLockingById(equipmentDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found."));

        int reservedQuantity = reservationRepository.getTotalReservedQuantity(equipment, companyId);

        if (newQuantity < reservedQuantity) {
            throw new IllegalStateException("Can't update quantity value.");
        }
    }

    private boolean isEquipmentAvailable(Company company, Equipment equipment) {
        int inStockQuantity = company.getEquipmentQuantityInStock(equipment);
        int reservedQuantity = reservationRepository.getTotalReservedQuantity(equipment, company.getId());

        return inStockQuantity - reservedQuantity > 0;
    }
}
