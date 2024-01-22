package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.ReservationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.exception.EmailNotSentException;
import com.isa.med_equipment.exception.QRCodeGenerationException;
import com.isa.med_equipment.model.*;
import com.isa.med_equipment.repository.*;
import com.isa.med_equipment.service.ReservationService;
import com.isa.med_equipment.util.EmailSender;
import com.isa.med_equipment.util.Mapper;
import com.isa.med_equipment.util.QRCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final CompanyRepository companyRepository;
    private final ReservationRepository reservationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmailSender emailSender;
    private final Mapper mapper;

    public ReservationServiceImpl(Mapper mapper, UserRepository userRepository, CompanyRepository companyRepository, TimeSlotRepository timeSlotRepository, ReservationRepository reservationRepository, EquipmentRepository equipmentRepository, EmailSender emailSender) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.reservationRepository = reservationRepository;
        this.equipmentRepository = equipmentRepository;
        this.emailSender = emailSender;
    }

    @Override
    public Page<ReservationDto> findAllByUser(Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findByUser_Id(userId, pageable);
        return mapper.mapPage(reservations, ReservationDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationDto reserve(ReservationDto reservationDto) {
        User user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Company company = companyRepository.findById(reservationDto.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found."));

        TimeSlot timeSlot = timeSlotRepository.findById(reservationDto.getTimeSlotId())
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found."));

        if (timeSlot.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reserve a time slot in the past.");
        }

        if(!Objects.equals(timeSlot.getAdmin().getCompany(), company)) {
            throw new IllegalArgumentException("Admin doesn't work at the company.");
        }

        List<Equipment> equipment = equipmentRepository.findWithLockingAllByIdIn(reservationDto.getEquipmentIds());
        checkEquipmentAvailability(company, equipment);

        Reservation reservation = new Reservation();
        reservation.make((RegisteredUser) user, equipment, timeSlot);
        byte[] qrCode = generateQRCode(reservation);

        reservationRepository.save(reservation);

        sendEmailWithQRCode(user, qrCode);

        return mapper.map(reservation, ReservationDto.class);
    }

    @Override
    public UserDto getByTimeSlotId(Long id) {
        Reservation reservation = reservationRepository.getReservationByTimeSlotId(id);
        Optional<User> user = userRepository.findById(reservation.getUser().getId());
        return mapper.map(user, UserDto.class);
    }

    private byte[] generateQRCode(Reservation reservation) {
        byte[] qrCode = QRCodeGenerator.generateReservationQRCode(reservation);
        reservation.setQrCode(qrCode);
        return qrCode;
    }

    private void checkEquipmentAvailability(Company company, List<Equipment> equipment) {
        for (Equipment equip : equipment) {
            int inStockQuantity = company.getEquipmentQuantityInStock(equip);
            int reservedQuantity = reservationRepository.getTotalReservedQuantity(equip, company.getId());

            int availableQuantity = inStockQuantity - reservedQuantity;
            if (availableQuantity <= 0) {
                throw new IllegalStateException("No equipment available: " + equip.getName());
            }
        }
    }

    @Async
    public void sendEmailWithQRCode(User user, byte[] qrCodeByteArray) {
        if (qrCodeByteArray != null) {
            String registrationSubject = "Details about your reservation.";
            String registrationMessage = "Scan the QR code to be able to see details about your reservation.";
            try {
                emailSender.sendEmailWithAttachment(user, registrationSubject, registrationMessage, qrCodeByteArray, "reservation_qr_code.png");
            } catch (MessagingException e) {
                throw new EmailNotSentException("Error sending email with attachment" + e);
            }
        } else {
            throw new QRCodeGenerationException("Failed to generate QR code for reservation " + user.getId());
        }
    }
}