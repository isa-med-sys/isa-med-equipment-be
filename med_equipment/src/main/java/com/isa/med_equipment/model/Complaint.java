package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    @JsonManagedReference
    private RegisteredUser user;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName="id")
    @JsonManagedReference
    private Company company;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName="id")
    @JsonManagedReference
    private CompanyAdmin admin;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "complaint_date", nullable = false)
    private LocalDateTime complaintDate;
}
