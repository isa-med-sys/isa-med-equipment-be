package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Contract findByUserIdAndIsActiveTrue(Long userId);
    List<Contract> findByUserId(Long userId);
    List<Contract> findByIsActiveTrue();
    Page<Contract> findAllByCompanyIdAndIsActiveTrue(Long companyId, Pageable pageable);
}