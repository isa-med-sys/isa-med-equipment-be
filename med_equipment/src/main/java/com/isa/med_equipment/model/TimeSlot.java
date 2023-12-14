package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName="id")
    @JsonManagedReference
    private CompanyAdmin admin;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "is_free")
    private Boolean isFree;

    @Version
    private Long version;

    public static final Duration DURATION = Duration.ofMinutes(30);

    public void reserve() {
        if (!isFree) {
            throw new IllegalStateException("Time Slot isn't free.");
        }
        isFree = false;
    }
}