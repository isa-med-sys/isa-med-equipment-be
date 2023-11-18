package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<List<Company>> findAll() {
        return new ResponseEntity<>(companyService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/equipment/{id}")
    public ResponseEntity<List<Company>> findAllByEquipment(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.findAllByEquipment(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Optional<Company>> getById(@PathVariable Long id) {
        Optional<Company> company = companyService.findById(id);
        return company.isPresent() ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<?> add(@RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.add(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/{id}")
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
