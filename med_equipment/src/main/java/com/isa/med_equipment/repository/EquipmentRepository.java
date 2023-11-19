package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Company;
import com.isa.med_equipment.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    //Page<Equipment> findAll(Specification<Equipment> specification, Pageable pageable);
}
