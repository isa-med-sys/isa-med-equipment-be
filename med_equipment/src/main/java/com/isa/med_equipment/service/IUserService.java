package com.isa.med_equipment.service;

import com.isa.med_equipment.beans.User;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.validation.EmailExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User register(UserDto userDto) throws EmailExistsException;
    boolean emailExists(String email);
}
