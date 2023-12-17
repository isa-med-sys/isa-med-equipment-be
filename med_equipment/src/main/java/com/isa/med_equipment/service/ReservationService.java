package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    Page<ReservationDto> findAllByUser(Long userId, Pageable pageable);
    ReservationDto reserve(ReservationDto reservationDto);
    UserDto getByTimeSlotId(Long id);

}