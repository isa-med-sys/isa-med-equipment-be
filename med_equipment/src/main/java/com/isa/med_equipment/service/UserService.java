package com.isa.med_equipment.service;

import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.exception.IncorrectPasswordException;
import com.isa.med_equipment.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User register(UserRegistrationDto userRegistrationDto) throws EmailExistsException;
    boolean emailExists(String email);
    User confirmRegistration(String token);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> update(Long userId, UserUpdateDto UserUpdateDto) throws IncorrectPasswordException;
}
