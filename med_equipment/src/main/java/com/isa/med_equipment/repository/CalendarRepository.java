package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long>, JpaSpecificationExecutor<Calendar> {

    Optional<Calendar> findByCompany_Id(Long companyId);
}
