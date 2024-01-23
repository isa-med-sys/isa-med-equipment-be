package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.EquipmentDto;
import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {

    Page<ReservationDto> findPastByUser(Long userId, Pageable pageable);
    Page<ReservationDto> findUpcomingByUser(Long userId, Pageable pageable);
    ReservationDto reserve(ReservationDto reservationDto);
    UserDto getByTimeSlotId(Long id);
    Boolean canUpdateEquipment(Long companyId, EquipmentDto equipmentDto);
    Boolean canDeleteEquipment(Long companyId, Long equipmentId);
}