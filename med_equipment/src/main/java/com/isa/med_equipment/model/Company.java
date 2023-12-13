package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rating")
    private Float rating;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //cascade type
    @JoinColumn(name = "address_id", referencedColumnName="id")
    private Address address;

    @ElementCollection
    @CollectionTable(
            name = "company_equipment",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @MapKeyJoinColumn(name = "equipment_id")
    @Column(name = "quantity", nullable = false)
    @JsonManagedReference
    private Map<Equipment, Integer> equipment = new HashMap<>();


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<CompanyAdmin> admins = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "calendar_id", referencedColumnName="id")
    private Calendar calendar;
}
