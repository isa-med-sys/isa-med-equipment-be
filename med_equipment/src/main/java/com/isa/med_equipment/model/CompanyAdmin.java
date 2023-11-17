package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_admins")
public class CompanyAdmin extends User {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", referencedColumnName="id")
    private Company company;
    @Override
    public Role getRole() {
        return Role.COMPANY_ADMIN;
    }
}

