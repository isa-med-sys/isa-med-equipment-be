package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CompanyDto;
import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "http://localhost:4200")
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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Optional<Company>> getById(@PathVariable Long id) {
        Optional<Company> company = companyService.findById(id);
        return company.isPresent() ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public ResponseEntity<?> add(@RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.add(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.update(id, companyDto);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
