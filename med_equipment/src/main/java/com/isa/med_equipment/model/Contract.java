package com.isa.med_equipment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Column(name = "last_delivery_date")
    private LocalDate lastDeliveryDate;

    public List<Long> getEquipmentIds() {
        return new ArrayList<>(equipmentQuantities.keySet());
    }

    public List<Integer> getQuantities() {
        return new ArrayList<>(equipmentQuantities.values());
    }

    public LocalDate calculateNextDeliveryDate() {
        if (lastDeliveryDate != null) {
            return lastDeliveryDate.plusMonths(1);
        } else if (startDate.isBefore(LocalDate.now())) {
            return startDate.plusMonths(1);
        } else {
            return startDate;

        }
    }

    public void updateLastDeliveryDate() {
        this.lastDeliveryDate = LocalDate.now();
    }

    public void deactivate() {
        if (!isActive) {
            throw new IllegalStateException("Contract is already inactive.");
        }

        isActive = false;
    }
}
