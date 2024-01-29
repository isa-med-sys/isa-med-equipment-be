package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.CompanyAdmin;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Page<Company> findAll(Specification<Company> specification, Pageable pageable);;

    @Query("SELECT ca FROM CompanyAdmin ca WHERE ca.company.id = :companyId")
    List<CompanyAdmin> findAdminsByCompany(Long companyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Company c where c.id = :id")
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value ="0")})
    Optional<Company> findWithLockingById(Long id);
}
