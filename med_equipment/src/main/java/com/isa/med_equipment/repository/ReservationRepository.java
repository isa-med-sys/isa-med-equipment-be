package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE :equipment MEMBER OF r.equipment " +
            "AND r.timeSlot.admin.company.id = :companyId " +
            "AND r.timeSlot.start > CURRENT_TIMESTAMP " +
            "AND r.isCancelled = false")
    int getTotalReservedQuantity(@Param("equipment") Equipment equipment, @Param("companyId") Long companyId);

    Page<Reservation> findByUser_Id(Long userId, Pageable pageable);
}