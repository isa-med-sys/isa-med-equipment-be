package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Page<Company> findAll(Specification<Company> specification, Pageable pageable);;

    @Query("SELECT ca FROM CompanyAdmin ca WHERE ca.company.id = :companyId")
    List<CompanyAdmin> findAdminsByCompany(Long companyId);
}
