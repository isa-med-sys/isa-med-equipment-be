package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Company company;

    @Column(name = "has_changed_password", nullable = false)
    private Boolean hasChangedPassword;

    @Override
    public Role getRole() {
        return Role.COMPANY_ADMIN;
    }
}

