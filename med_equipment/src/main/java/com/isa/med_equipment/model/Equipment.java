package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "rating")
    private Float rating;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private EquipmentType type;

    @ManyToMany(mappedBy = "equipment")
    @JsonIgnore
    private Set<Company> companies = new HashSet<>();
}
