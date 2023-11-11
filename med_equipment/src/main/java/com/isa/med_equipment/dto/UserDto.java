package com.isa.med_equipment.dto;

import jakarta.validation.constraints.*;
import com.isa.med_equipment.beans.Address;

public class UserDto {
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

    @NotEmpty(message = "Occupation is required")
    private String occupation;

    @NotEmpty(message = "Company information number is required")
    private String companyInfo;

	@NotEmpty(message = "Address is required")
	private Address address;

    public UserDto() {

    }

	public UserDto(@NotEmpty(message = "Name is required") @Size(min = 2, max = 32) String name,
			@NotEmpty(message = "Surname is required") @Size(min = 2, max = 32) String surname,
			@NotEmpty(message = "Email is required") @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") String email,
			@NotEmpty(message = "Password is required") String password,
			@NotEmpty(message = "Phone number is required") String phoneNumber,
			@NotEmpty(message = "Occupation is required") String occupation,
			@NotEmpty(message = "Company information number is required") String companyInfo,
			@NotEmpty(message = "Address is required") Address address) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.occupation = occupation;
		this.companyInfo = companyInfo;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}