package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "duration")
    private Integer duration;
}
