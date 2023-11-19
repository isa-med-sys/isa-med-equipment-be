package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    @JsonManagedReference
    private RegisteredUser user;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName="id")
    @JsonManagedReference
    private Equipment equipment;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "time_slot_id", referencedColumnName="id")
    private TimeSlot  timeSlot;

    @Column(name = "is_picked_up")
    private Boolean isPickedUp;
}
