package com.isa.med_equipment.dto;

import com.isa.med_equipment.model.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 32)
    private String name;

    @NotEmpty(message = "Surname is required")
    @Size(min = 2, max = 32)
    private String surname;

    @NotEmpty(message = "Current password is required")
    @Size(min = 5, max = 32)
    private String currentPassword;

    @Size(min = 5, max = 32)
    private String newPassword;

    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;

    @NotEmpty(message = "Occupation is required")
    private String occupation;

    @NotEmpty(message = "Company information number is required")
    private String companyInfo;

    @NotEmpty(message = "Address is required")
    private Address address;

    public UserUpdateDto() {

    }

    public UserUpdateDto(@NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
                               @NotEmpty(message = "Surname is required") @Size(min = 2, max = 32) String surname,
                               @NotEmpty(message = "Current Password is required") String currentPassword, String newPassword,
                               @NotEmpty(message = "Phone Number is required") String phoneNumber,
                               @NotEmpty(message = "Occupation is required") String occupation,
                               @NotEmpty(message = "Company Information number is required") String companyInfo,
                               @NotEmpty(message = "Address is required") Address address) {
        super();
        this.name = name;
        this.surname = surname;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.companyInfo = companyInfo;
        this.address = address;
    }
}
