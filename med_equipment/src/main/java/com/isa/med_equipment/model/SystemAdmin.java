package com.isa.med_equipment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "system_admins")
public class SystemAdmin extends User {

    @Column(name = "has_changed_password", nullable = false)
    private Boolean hasChangedPassword;

    @Override
    public Role getRole() {
        return Role.SYSTEM_ADMIN;
    }
}
