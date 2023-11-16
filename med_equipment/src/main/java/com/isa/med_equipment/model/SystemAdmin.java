package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "system_admins")
public class SystemAdmin extends User {

    @Override
    public Role getRole() {
        return Role.SYSTEM_ADMIN;
    }
}
