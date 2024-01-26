package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "contract_equipment",
            joinColumns = @JoinColumn(name = "contract_id")
    )
    @MapKeyColumn(name = "equipment_id")
    @Column(name = "quantity", nullable = false)
    private Map<Long, Integer> equipmentQuantities = new HashMap<>();

    @Column(name = "is_active")
    private Boolean isActive;

    public void deactivate() {
        if (!isActive) {
            throw new IllegalStateException("Contract is already inactive.");
        }

        isActive = false;
    }
}
