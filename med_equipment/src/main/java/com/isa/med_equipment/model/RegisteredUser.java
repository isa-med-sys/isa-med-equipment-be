package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "registered_users")
public class RegisteredUser extends User {

    @Column(name = "occupation", nullable = false)
    private String occupation;

    @Column(name = "company_info", nullable = false)
    private String companyInfo;

    @Column(name = "penalty_points", nullable = false)
    private Integer penaltyPoints = 0;

    // TODO
    // Loyalty Program

    @Override
    public Role getRole() {
        return Role.REGISTERED_USER;
    }
}
