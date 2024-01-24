package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id", nullable = false)
    @JsonManagedReference
    private RegisteredUser user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservation_equipment",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    @JsonManagedReference
    private List<Equipment> equipment;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "time_slot_id", referencedColumnName="id", nullable = false)
    private TimeSlot  timeSlot;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_picked_up", nullable = false)
    private Boolean isPickedUp = false;

    @Column(name = "is_cancelled", nullable = false)
    private Boolean isCancelled = false;

    @Column(name = "qr_code")
    private byte[] qrCode;

    @Version
    private Long  version;

    public void make(RegisteredUser user, List<Equipment> equipment, TimeSlot timeSlot){
        timeSlot.reserve();

        double totalPrice = equipment.stream()
                .mapToDouble(Equipment::getPrice)
                .sum();

        this.setUser(user);
        this.setEquipment(equipment);
        this.setPrice(totalPrice);
        this.setTimeSlot(timeSlot);
    }

    public void cancel() {
        if (isCancelled) {
            throw new IllegalStateException("Reservation is already cancelled.");
        }

        user.updatePenaltyPointsOnCancellation(timeSlot.getStart());
        timeSlot.markAsFree();
        isCancelled = true;
    }
}