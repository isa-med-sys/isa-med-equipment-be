package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<Page<CompanyDto>> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "city", required = false) String city,
                                                    @RequestParam(name = "rating", required = false) Float rating) {
        Page<CompanyDto> result = companyService.findAll(name, city, rating,  PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable Long id) {
        CompanyDto result = companyService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<List<Company>> findAllByEquipment(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.findAllByEquipment(id), HttpStatus.OK);
    }

    @GetMapping("admins/{id}")
    public ResponseEntity<List<CompanyAdmin>> findAllAdmins(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.findAllAdmins(id) , HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<?> add(@RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.add(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_COMPANY_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.update(id, companyDto);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
