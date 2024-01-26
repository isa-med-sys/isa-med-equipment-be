package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.model.Contract;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.repository.ContractRepository;
import com.isa.med_equipment.repository.EquipmentRepository;
import com.isa.med_equipment.service.ContractService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final EquipmentRepository equipmentRepository;
    private final Mapper mapper;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, EquipmentRepository equipmentRepository, Mapper mapper) {
        this.contractRepository = contractRepository;
        this.equipmentRepository = equipmentRepository;
        this.mapper = mapper;
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
}