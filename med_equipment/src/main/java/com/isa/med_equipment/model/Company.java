package com.isa.med_equipment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName="id")
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "company_equipment",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @MapKeyJoinColumn(name = "equipment_id")
    @Column(name = "quantity", nullable = false)
    @JsonManagedReference
    @Cascade({})
    private Map<Equipment, Integer> equipment = new HashMap<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<CompanyAdmin> admins = new ArrayList<>();

    @Version
    private Long version;

    public int getEquipmentQuantityInStock(Equipment equipment) {
        if (this.equipment.containsKey(equipment)){
            return this.equipment.getOrDefault(equipment, 0);
        }
        throw new EntityNotFoundException("Equipment not found.");
    }

    @Override
    public String toString() {
        return String.format("%s\n%s %s, %s, %s", name, address.getStreet(), address.getStreetNumber(), address.getCity(), address.getCountry());
    }
}
