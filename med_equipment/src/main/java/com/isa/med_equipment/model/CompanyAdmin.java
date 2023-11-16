package com.isa.med_equipment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "company_admins")
public class CompanyAdmin extends User {

    @Override
    public Role getRole() {
        return Role.COMPANY_ADMIN;
    }
}

