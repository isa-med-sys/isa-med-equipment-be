package com.isa.med_equipment.repository;

import com.isa.med_equipment.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}