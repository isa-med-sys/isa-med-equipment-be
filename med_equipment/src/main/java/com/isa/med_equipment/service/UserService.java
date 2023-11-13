package com.isa.med_equipment.service;

import com.isa.med_equipment.model.User;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.exception.EmailExistsException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User register(UserDto userDto) throws EmailExistsException;
    boolean emailExists(String email);
    User confirmRegistration(String token);
    List<User> findAll();
    Optional<User> findById(Long id);
}
