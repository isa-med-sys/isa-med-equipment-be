package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyAdminRegistrationDto {

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 32)
    private String name;

    @NotEmpty(message = "Surname is required")
    @Size(min = 2, max = 32)
    private String surname;

    @NotEmpty(message = "Email is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;

    @NotEmpty(message = "Address is required")
    private Address address;

    private Role role;

    @NotEmpty(message = "Company is required")
    private Long companyId;

    public CompanyAdminRegistrationDto(@NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
                                        @NotEmpty(message = "Surname is required") @Size(min = 2, max = 32) String surname,
                                        @NotEmpty(message = "Email is required") @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String email,
                                        @NotEmpty(message = "Password is required") String password,
                                        @NotEmpty(message = "Phone number is required") String phoneNumber,
                                        @NotEmpty(message = "Address is required") Address address,
                                        @NotEmpty(message = "Company is required") Long companyId, Role role) {
        super();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.companyId = companyId;
        this.role = role;
    }
}