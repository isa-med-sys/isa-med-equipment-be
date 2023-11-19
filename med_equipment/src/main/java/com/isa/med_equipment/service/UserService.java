package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.CompanyAdminRegistrationDto;
import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.exception.IncorrectPasswordException;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User register(UserRegistrationDto userRegistrationDto) throws EmailExistsException;
    Optional<User> emailExists(String email);
    User confirmRegistration(String token);
    List<User> findAll();
    List<CompanyAdmin> findByCompanyId(Long id);
    Optional<User> findById(Long id);
    Optional<User> update(Long userId, UserUpdateDto UserUpdateDto) throws IncorrectPasswordException;
    CompanyAdmin registerCompanyAdmin(CompanyAdminRegistrationDto companyAdminRegistrationDto) throws EmailExistsException;
}