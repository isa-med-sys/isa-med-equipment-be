package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.OrderDto;
import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.model.RegisteredUser;
import com.isa.med_equipment.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public interface ReservationService {

    Page<ReservationDto> findPastByUser(Long userId, Pageable pageable);
    Page<ReservationDto> findUpcomingByUser(Long userId, Pageable pageable);
    ReservationDto reserve(ReservationDto reservationDto);
    OrderDto findByCode(Long userId, InputStream fileInputStream) throws IOException;
    OrderDto findOrderById(Long userId, Long orderId);
    UserDto getByTimeSlotId(Long id);
    ReservationDto cancelReservation(ReservationDto reservationDto);
    ReservationDto completeReservation(ReservationDto reservationDto);
    Page<ReservationDto> findAllByCompany(Long companyId, Pageable pageable);
    Page<UserDto> findAllUsers(Long companyId, Pageable pageable);
}