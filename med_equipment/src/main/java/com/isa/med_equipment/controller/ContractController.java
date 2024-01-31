package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.ContractDto;
import com.isa.med_equipment.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<Page<ContractDto>> findAllContractsByCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam Long companyId) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ContractDto> result = contractService.findAllActiveByCompany(companyId, pageRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/start/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<Void> startDelivery(@PathVariable Long id){
        contractService.startDelivery(id);
        return ResponseEntity.ok().build();
    }
}
