package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    @Column(name = "works_on_weekends", nullable = false)
    private Boolean worksOnWeekends;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "calendar_id")
    @JsonBackReference
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @Version
    private Long version;

    public void addTimeSlot(TimeSlot timeSlot) {
        if (!isWorkingDay(timeSlot.getStart())) {
            throw new IllegalArgumentException("Time Slot is not on a working day.");
        }

        if (!isWithinWorkingHours(timeSlot.getStart().toLocalTime())) {
            throw new IllegalArgumentException("Time Slot is not within work hours.");
        }

        timeSlots.add(timeSlot);
    }

    private boolean isWithinWorkingHours(LocalTime time) {
        return !time.isBefore(workStartTime)
                && !time.isAfter(workEndTime.minusMinutes(TimeSlot.DURATION.toMinutes()));
    }

    private boolean isWorkingDay(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return worksOnWeekends || (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);
    }
}
