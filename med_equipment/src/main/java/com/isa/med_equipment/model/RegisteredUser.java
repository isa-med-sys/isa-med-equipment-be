package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "registered_users")
public class RegisteredUser extends User {

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "company_info", nullable = false)
    private String companyInfo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name = "penalty_points", nullable = false)
    private Integer penaltyPoints = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @Override
    public Role getRole() {
        return Role.REGISTERED_USER;
    }

    public void updatePenaltyPointsOnCancellation(LocalDateTime reservationStartTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.plusHours(24).isAfter(reservationStartTime)) {
            penaltyPoints += 2;
        } else {
            penaltyPoints += 1;
        }
    }
}