package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompanyRegistrationDto {

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
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Boolean worksOnWeekends;
}