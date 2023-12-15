package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "company_admins")
public class CompanyAdmin extends User {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @JsonManagedReference
    private Company company;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeSlot> timeSlots;
    
    @Column(name = "has_changed_password", nullable = false)
    private Boolean hasChangedPassword;

    @Override
    public Role getRole() {
        return Role.COMPANY_ADMIN;
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        for (TimeSlot existingTimeSlot : timeSlots) {
            if (isOverlapping(existingTimeSlot, timeSlot)) {
                throw new IllegalArgumentException("Admin isn't free for the time slot.");
            }
        }

        timeSlot.setAdmin(this);
        timeSlots.add(timeSlot);
    }

    private boolean isOverlapping(TimeSlot timeSlot1, TimeSlot timeSlot2) {
        LocalDateTime start1 = timeSlot1.getStart();
        LocalDateTime end1 = start1.plus(TimeSlot.DURATION);

        LocalDateTime start2 = timeSlot2.getStart();
        LocalDateTime end2 = start2.plus(TimeSlot.DURATION);

        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}

