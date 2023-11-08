package com.isa.service;

import com.isa.beans.User;
import com.isa.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User register(UserDto userDto);
}
