package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByAdmin_Company_IdAndStartAfterAndIsFree(Long companyId, LocalDateTime now, boolean b);
}
