package com.isa.med_equipment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CompanyAdminDto {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Long companyId;
}
