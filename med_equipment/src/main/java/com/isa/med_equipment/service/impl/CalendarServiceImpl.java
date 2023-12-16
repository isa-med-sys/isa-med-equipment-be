package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CalendarDto;
import com.isa.med_equipment.dto.TimeSlotDto;
import com.isa.med_equipment.model.Calendar;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.TimeSlot;
import com.isa.med_equipment.repository.CalendarRepository;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.TimeSlotRepository;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.CalendarService;
import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public CalendarServiceImpl(CalendarRepository calendarRepository,
                               UserRepository userRepository,
                               TimeSlotRepository timeSlotRepository,
                               CompanyRepository companyRepository,
                               Mapper mapper) {
        this.calendarRepository = calendarRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public CalendarDto findByCompany(Long companyId) {
        Calendar calendar = calendarRepository.findByCompany_Id(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found"));

        return mapper.map(calendar, CalendarDto.class);
    }

    @Override
    public List<TimeSlotDto> findFreePredefinedTimeSlotsByCompany(Long companyId) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByAdmin_Company_IdAndStartAfterAndIsFree(
                companyId,
                LocalDateTime.now(),
                true);
        return mapper.mapList(timeSlots, TimeSlotDto.class);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public TimeSlotDto createTimeSlot(Long companyId, TimeSlotDto timeSlotDto) {
        CompanyAdmin admin;
        if (timeSlotDto.getCompanyAdminId() == null) {
            admin = findFreeAdmin(companyId, timeSlotDto);
        } else {
            admin = (CompanyAdmin) userRepository.findById(timeSlotDto.getCompanyAdminId())
                    .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        }

        Calendar calendar = calendarRepository.findByCompany_Id(admin.getCompany().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Calendar not found"));

        if (timeSlotDto.getStart() != null && timeSlotDto.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create a time slot in the past.");
        }

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStart(timeSlotDto.getStart());
        admin.addTimeSlot(timeSlot);
        calendar.addTimeSlot(timeSlot);

        TimeSlot newTimeSlot = timeSlotRepository.save(timeSlot);
        return mapper.map(newTimeSlot, TimeSlotDto.class);
    }

    private CompanyAdmin findFreeAdmin(Long companyId, TimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = mapper.map(timeSlotDto, TimeSlot.class);
        List<CompanyAdmin> admins = companyRepository.findAdminsByCompany(companyId);
        for(CompanyAdmin admin : admins) {
            if (admin.isFreeForTimeSlot(timeSlot)){
                return admin;
            }
        }
        throw new IllegalStateException("No admin is free for wanted time slot.");
    }
}