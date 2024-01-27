package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.dto.StartDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.Contract;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.RegisteredUser;
import com.isa.med_equipment.rabbitMQ.RabbitMQProducerLocation;
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
    private final RabbitMQProducerLocation producer;
    private final Mapper mapper;

    @Autowired
    public ContractServiceImpl(
            ContractRepository contractRepository,
            CompanyRepository companyRepository,
            EquipmentRepository equipmentRepository,
            ReservationRepository reservationRepository,
            RegisteredUserRepository userRepository,
            RabbitMQProducerLocation producer,
            Mapper mapper) {
        this.contractRepository = contractRepository;
        this.companyRepository = companyRepository;
        this.equipmentRepository = equipmentRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.producer = producer;
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

        List<Contract> contractList = contracts.getContent();
        List<ContractDto> contractDtoList = contractsDto.getContent();

        for (Contract contract : contractList) {
            ContractDto contractDto = contractDtoList.get(contractList.indexOf(contract));

            boolean canStart = contract.calculateNextDeliveryDate().isEqual(LocalDate.now());
            contractDto.setCanStart(canStart);
        }

        populateEquipment(contractsDto);
        return contractsDto;
    }

    @Override
    public void startDelivery(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found."));
        Company company = companyRepository.findById(contract.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found."));
        RegisteredUser registeredUser = userRepository.findById(contract.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        if(updateEquipmentAndContract(contract, company)) {
            StartDto start = new StartDto(contract.getCompanyId(), company.getAddress().getLongitude(),
                    company.getAddress().getLatitude(), registeredUser.getAddress().getLongitude(), registeredUser.getAddress().getLatitude());
            producer.sendMessage(start);
        }
    }

    @Transactional
    public boolean updateEquipmentAndContract(Contract contract, Company company) {
        contract.updateLastDeliveryDate();

        Map<Equipment, Integer> eq = company.getEquipment();
        Map<Long, Integer> ce = contract.getEquipmentQuantities();

        for (Map.Entry<Long, Integer> entry : ce.entrySet()) {
            Long equipmentId = entry.getKey();
            Integer quantityToDeduct = entry.getValue();
            Equipment equipmentToUpdate = findEquipmentById(eq, equipmentId);

            if (equipmentToUpdate != null) {
                int currentQuantity = eq.get(equipmentToUpdate);
                int updatedQuantity = currentQuantity - quantityToDeduct;
                if (updatedQuantity < 0) {
                    throw new IllegalArgumentException("Negative quantity not allowed for equipment.");
                }
                eq.put(equipmentToUpdate, updatedQuantity);
            }
        }
        company.setEquipment(eq);
        return true;
    }

    private Equipment findEquipmentById(Map<Equipment, Integer> eq, Long equipmentId) {
        for (Map.Entry<Equipment, Integer> entry : eq.entrySet()) {
            if (entry.getKey().getId().equals(equipmentId)) {
                return entry.getKey();
            }
        }
        return null;
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