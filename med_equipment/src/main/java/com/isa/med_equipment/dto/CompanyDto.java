package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.Equipment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDto {

    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 32)
    private String name;

    @NotEmpty(message = "Description is required")
    @Size(min = 2, max = 32)
    private String description;

    @NotEmpty(message = "Address is required")
    private Address address;

    private Float rating;

    private Set<Equipment> equipment; //

    private List<CompanyAdmin> companyAdmins; //

    public CompanyDto(Long id, @NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
                      @NotEmpty(message = "Description is required") @Size(min = 2, max = 32) String description,
                      @NotEmpty(message = "Address is required") Address address,
                      Float rating, Set<Equipment> equipment, List<CompanyAdmin> companyAdmins) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.equipment = equipment;
        this.companyAdmins = companyAdmins;
    }
}
