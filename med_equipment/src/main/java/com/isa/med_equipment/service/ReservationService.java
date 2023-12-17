package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import org.springframework.stereotype.Service;
@Service
public interface ReservationService {
    ReservationDto reserve(ReservationDto reservationDto);
    UserDto getByTimeSlotId(Long id);

}