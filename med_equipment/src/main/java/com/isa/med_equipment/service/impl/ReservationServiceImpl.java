package com.isa.med_equipment.service.impl;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.isa.med_equipment.dto.OrderDto;
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
import jakarta.persistence.criteria.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final UserRepository userRepository;
    private final RegisteredUserRepository registeredUserRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final CompanyRepository companyRepository;
    private final ReservationRepository reservationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmailSender emailSender;
    private final Mapper mapper;

    public ReservationServiceImpl(Mapper mapper,
                                  UserRepository userRepository,
                                  RegisteredUserRepository registeredUserRepository,
                                  CompanyRepository companyRepository,
                                  TimeSlotRepository timeSlotRepository,
                                  ReservationRepository reservationRepository,
                                  EquipmentRepository equipmentRepository,
                                  EmailSender emailSender) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.registeredUserRepository = registeredUserRepository;
        this.companyRepository = companyRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.reservationRepository = reservationRepository;
        this.equipmentRepository = equipmentRepository;
        this.emailSender = emailSender;
    }

    @Override
    public Page<ReservationDto> findPastByUser(Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findPastByUser(userId, pageable);
        return populateReservations(reservations);
    }

    @Override
    public Page<ReservationDto> findUpcomingByUser(Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findUpcomingByUser(userId, pageable);
        return populateReservations(reservations);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationDto reserve(ReservationDto reservationDto) {
        RegisteredUser user = registeredUserRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Company company = companyRepository.findById(reservationDto.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found."));

        TimeSlot timeSlot = timeSlotRepository.findById(reservationDto.getTimeSlotId())
                .orElseThrow(() -> new EntityNotFoundException("Time slot not found."));

        validateReservation(user, company, timeSlot);

        List<Equipment> equipment = equipmentRepository.findWithLockingAllByIdIn(reservationDto.getEquipmentIds());
        checkEquipmentAvailability(company, equipment);

        Reservation reservation = new Reservation();
        reservation.make(user, equipment, timeSlot);

        reservation = reservationRepository.save(reservation);
      
        byte[] qrCode = generateQRCode(reservation);

        sendEmailWithQRCode(user, qrCode);

        return mapper.map(reservation, ReservationDto.class);
    }

    private void validateReservation(RegisteredUser user, Company company, TimeSlot timeSlot) {
        if (timeSlot.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reserve a time slot in the past.");
        }

        if (!Objects.equals(timeSlot.getAdmin().getCompany(), company)) {
            throw new IllegalArgumentException("Admin doesn't work at the company.");
        }

        if (reservationRepository.hasCanceledReservationInTimeslot(user.getId(), timeSlot.getId())) {
            throw new IllegalStateException("Cannot reserve timeslot that you have already cancelled.");
        }

        if (reservationRepository.findByTimeSlotIdAndIsCancelledIsFalse(timeSlot.getId()) != null) {
            throw new IllegalStateException("Time slot is already reserved and not free.");
        }

        if (user.getPenaltyPoints() >= 3) {
            throw new IllegalArgumentException("Reservation can't be made if you have 3 or more penalty points.");
        }
    }

    @Override
    public OrderDto findByCode(Long userId, InputStream fileInputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(fileInputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            String resultText = result.getText();

            String reservationNumberPrefix = "Reservation Number: ";
            int startIndex = resultText.indexOf(reservationNumberPrefix) + reservationNumberPrefix.length();
            int endIndex = resultText.indexOf("\n", startIndex);
            String reservationNumberString = resultText.substring(startIndex, endIndex).trim();

            return getOrder(Long.parseLong(reservationNumberString), userId);
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.out.println("Failed to extract Data");
            return null;
        }
    }

    public OrderDto findOrderById(Long userId, Long orderId) {
        return getOrder(orderId, userId);
    }

    private OrderDto getOrder(Long id, Long userId) {
        OrderDto order = new OrderDto();

        order.setId(id);

        Reservation reservation = reservationRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        order.setCustomer(reservation.getUser().getName() + " " + reservation.getUser().getSurname());
        order.setTimeslotStart(reservation.getTimeSlot().getStart());
        order.setTimeslotEnd(order.getTimeslotStart().plusMinutes(TimeSlot.DURATION.toMinutes()));
        List<String> equipment = new ArrayList<>();
        for (Equipment e : reservation.getEquipment()) {
            equipment.add(e.getName());
        }
        order.setEquipment(equipment);
        order.setIsValid(new Date().before(Date.from(order.getTimeslotEnd().atZone(ZoneId.systemDefault()).toInstant())));
        order.setIsTaken(reservation.getIsPickedUp());
        order.setIsCanceled(reservation.getIsCancelled());
        order.setIsRightAdmin(Objects.equals(userId, reservation.getTimeSlot().getAdmin().getId()));
        if(!order.getIsValid() && !order.getIsCanceled()) {
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setId(id);
            cancelReservation(reservationDto);
        }
        return order;
    }

    @Override
    public UserDto getByTimeSlotId(Long id) {
        Reservation reservation = reservationRepository.findByTimeSlotIdAndIsCancelledIsFalse(id);
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

    @Async
    public void sendConfirmationEmail(User user) {
        String registrationSubject = "Your order was successfully taken.";
        String registrationMessage = "Bottom text.";
        try {
            emailSender.sendEmail(user, registrationSubject, registrationMessage);
        } catch (Exception e) {
            throw new EmailNotSentException("Error sending email" + e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationDto cancelReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(reservationDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        reservation.cancel();
        return mapper.map(reservation, ReservationDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationDto completeReservation(ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(reservationDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found."));

        if (!reservation.getIsCancelled() && !reservation.getIsPickedUp()) {
            reservation.setIsPickedUp(true);
            reservationRepository.save(reservation);
            updateCompanyEquipment(reservation);
            sendConfirmationEmail(reservation.getUser());
        }
        return mapper.map(reservation, ReservationDto.class);
    }

    private void updateCompanyEquipment(Reservation reservation) {
        Long id = reservation.getTimeSlot().getAdmin().getCompany().getId();
        Company company = companyRepository.findWithLockingById(id).orElseThrow(() -> new EntityNotFoundException("Company not found."));
        Map<Equipment, Integer> equipment = new HashMap<>(company.getEquipment());

        for (Equipment e : reservation.getEquipment()) {
            Integer currentValue = equipment.get(e);
            if (currentValue != null) {
                equipment.put(e, currentValue - 1);
            }
        }
        company.setEquipment(equipment);
        companyRepository.save(company);
    }
  
    private Page<ReservationDto> populateReservations(Page<Reservation> reservations) {
        Page<ReservationDto> reservationDtos = mapper.mapPage(reservations, ReservationDto.class);
        for(ReservationDto res : reservationDtos) {
            TimeSlot timeslot = timeSlotRepository.findById(res.getTimeSlotId()).orElseThrow();
            res.setCompanyName(timeslot.getAdmin().getCompany().getName());
            res.setStart(timeslot.getStart());
        }

        return reservationDtos;
    }

    @Override
    public Page<ReservationDto> findAllByCompany(Long companyId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAllByCompany(companyId, pageable);
        return populateReservations(reservations);
    }

    @Override
    public Page<UserDto> findAllUsers(Long companyId, Pageable pageable) {
        Page<RegisteredUser> users = reservationRepository.findAllUsers(companyId, pageable);
        return mapper.mapPage(users, UserDto.class);
    }
}