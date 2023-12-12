package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private String occupation;

    private String companyInfo;

    private Address address;

    private Integer penaltyPoints;

    private CompanyDto Company;
}
