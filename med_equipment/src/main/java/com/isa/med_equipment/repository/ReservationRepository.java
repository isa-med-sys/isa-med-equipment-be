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

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.timeSlot.id = :timeSlotId " +
            "AND r.isCancelled = true")
    boolean hasCanceledReservationInTimeslot(@Param("userId") Long userId, @Param("timeSlotId") Long timeSlotId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.timeSlot.id = :timeSlotId " +
            "AND r.isCancelled = false")
    Reservation findByTimeSlotIdAndIsCancelledIsFalse(@Param("timeSlotId") Long timeSlotId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.timeSlot.start <= CURRENT_TIMESTAMP")
    Page<Reservation> findPastByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.timeSlot.start > CURRENT_TIMESTAMP")
    Page<Reservation> findUpcomingByUser(@Param("userId") Long userId, Pageable pageable);

}