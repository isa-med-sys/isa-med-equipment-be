package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.ContractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ContractService {

    Page<ContractDto> findAllActiveByCompany(Long companyId, Pageable pageable);
}
