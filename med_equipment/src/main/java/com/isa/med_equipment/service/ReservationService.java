package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.ReservationDto;
import org.springframework.stereotype.Service;
@Service
public interface ReservationService {
    ReservationDto makeReservation(ReservationDto reservationDto);
}