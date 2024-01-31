package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Equipment;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Equipment e where e.id = :id")
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value ="0")})
    Optional<Equipment> findWithLockingById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Equipment e where e.id IN :ids")
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value ="0")})
    ArrayList<Equipment> findWithLockingAllByIdIn(List<Long> ids);

    Page<Equipment> findAll(Specification<Equipment> specification, Pageable pageable);
}