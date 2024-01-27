package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.Contract;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.RegisteredUser;
import com.isa.med_equipment.repository.*;
import com.isa.med_equipment.service.ContractService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final CompanyRepository companyRepository;
    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;
    private final RegisteredUserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public ContractServiceImpl(
            ContractRepository contractRepository,
            CompanyRepository companyRepository,
            EquipmentRepository equipmentRepository,
            ReservationRepository reservationRepository,
            RegisteredUserRepository userRepository,
            Mapper mapper) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.equipmentRepository = equipmentRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<ContractDto> findAllScheduledForDelivery() {
        LocalDate currentDate = LocalDate.now();
        int deliveryNoticeDays = 3;
        LocalDate targetDate = currentDate.plusDays(deliveryNoticeDays);

        List<Contract> active = contractRepository.findByIsActiveTrue();

         List<Contract> upcoming = active.stream()
                .filter(contract -> {
                    LocalDate nextDeliveryDate = contract.calculateNextDeliveryDate();
                    return nextDeliveryDate.isEqual(targetDate);
                })
                .collect(Collectors.toList());

         return  mapper.mapList(upcoming, ContractDto.class);
    }

    public Boolean canBeDelivered(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found."));

        Company company = companyRepository.findById(contract.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found."));

        List<Equipment> equipment = equipmentRepository.findWithLockingAllByIdIn(contract.getEquipmentIds());
        return checkEquipmentAvailability(company, equipment, contract.getQuantities());
    }

    @Override
    public Page<ContractDto> findAllActiveByCompany(Long companyId, Pageable pageable) {
        Page<Contract> contracts = contractRepository.findAllByCompanyIdAndIsActiveTrue(companyId, pageable);
        Page<ContractDto> contractsDto = mapper.mapPage(contracts, ContractDto.class);

        populateEquipment(contractsDto);
        return contracts == null
                ? null
                : contractsDto;
    }

    private void populateEquipment(Page<ContractDto> contractsDto) {
        for (ContractDto contract : contractsDto.getContent()) {
            RegisteredUser user = userRepository.findById(contract.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Hospital not found."));

            contract.setHospitalName(user.getCompanyInfo());
            Map<String, Integer> namedEquipmentQuantities = new HashMap<>();

            for (Map.Entry<Long, Integer> entry : contract.getEquipmentQuantities().entrySet()) {
                Long equipmentId = entry.getKey();
                Integer quantity = entry.getValue();

                Equipment equipment = equipmentRepository.findById(equipmentId)
                        .orElseThrow(() -> new EntityNotFoundException("Equipment not found."));

                namedEquipmentQuantities.put(equipment.getName(), quantity);
            }

            contract.setNamedEquipmentQuantities(namedEquipmentQuantities);
        }
    }

    private boolean checkEquipmentAvailability(Company company, List<Equipment> equipment, List<Integer> quantities) {
        for (int i = 0; i < equipment.size(); i++) {
            Equipment equip = equipment.get(i);
            int inStockQuantity = company.getEquipmentQuantityInStock(equip);
            int reservedQuantity = reservationRepository.getTotalReservedQuantity(equip, company.getId());
            int contractQuantity = quantities.get(i);

            int availableQuantity = inStockQuantity - reservedQuantity - contractQuantity;
            if (availableQuantity <= 0) {
                return false;
            }
        }
        return true;
    }
}