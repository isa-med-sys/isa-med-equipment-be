package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Equipment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDto {

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 32)
    private String name;

    @NotEmpty(message = "Address is required")
    private Address address;

    private float rating;

    private Set<Equipment> equipment;

    public CompanyDto(@NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
                      @NotEmpty(message = "Address is required") Address address,
                      float rating, Set<Equipment> equipment) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.equipment = equipment;
    }
}
