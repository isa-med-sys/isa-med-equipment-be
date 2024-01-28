package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.ContractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContractService {

    ContractDto findActiveByUser(Long userId);
    void handleReceived(ContractDto contractDto);
    void delete(Long contractId);
    List<ContractDto> findAllScheduledForDelivery();
    Boolean canBeDelivered(Long contractId);
    Page<ContractDto> findAllActiveByCompany(Long companyId, Pageable pageable);
}
