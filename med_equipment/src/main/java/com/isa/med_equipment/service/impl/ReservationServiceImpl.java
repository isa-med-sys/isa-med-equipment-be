package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.exception.EmailNotSentException;
import com.isa.med_equipment.exception.QRCodeGenerationException;
import com.isa.med_equipment.model.*;
import com.isa.med_equipment.repository.EquipmentRepository;
import com.isa.med_equipment.repository.ReservationRepository;
import com.isa.med_equipment.repository.TimeSlotRepository;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.ReservationService;
import com.isa.med_equipment.util.Mapper;
import com.isa.med_equipment.util.QRCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;
    private final EmailSenderService emailSenderService;
    private final Mapper mapper;

    public ReservationServiceImpl(Mapper mapper, UserRepository userRepository, EquipmentRepository equipmentRepository, TimeSlotRepository timeSlotRepository, ReservationRepository reservationRepository, EmailSenderService emailSenderService) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.reservationRepository = reservationRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public ReservationDto makeReservation(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Equipment equipment = equipmentRepository.findById(reservationDto.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

        TimeSlot timeSlot = timeSlotRepository.findById(reservationDto.getTimeSlotId())
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found"));

        Reservation reservation = createReservation(user, equipment, timeSlot);
        reservationRepository.save(reservation);

        byte[] qrCodeByteArray = QRCodeGenerator.generateQRCodeImageAsByteArray(reservation);
        sendEmailWithQRCode(user, qrCodeByteArray);

        return mapper.map(reservation, ReservationDto.class);
    }

    private Reservation createReservation(User user, Equipment equipment, TimeSlot timeSlot) {
        Reservation reservation = new Reservation();
        reservation.setUser((RegisteredUser) user);
        reservation.setEquipment(equipment);
        reservation.setTimeSlot(timeSlot);
        reservation.setIsPickedUp(false);

        return reservation;
    }

    private void sendEmailWithQRCode(User user, byte[] qrCodeByteArray) {
        if (qrCodeByteArray != null) {
            String registrationSubject = "Details about your reservation.";
            String registrationMessage = "Scan the QR code to be able to see details about your reservation.";
            try {
                emailSenderService.sendEmailWithAttachment(user, registrationSubject, registrationMessage, qrCodeByteArray, "reservation_qr_code.png");
            } catch (MessagingException e) {
                throw new EmailNotSentException("Error sending email with attachment" + e);
            }
        } else {
            throw new QRCodeGenerationException("Failed to generate QR code for reservation " + user.getId());
        }
    }
}