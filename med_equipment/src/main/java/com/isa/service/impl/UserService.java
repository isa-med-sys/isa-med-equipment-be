package com.isa.service.impl;

import java.util.List;
import java.util.Optional;
import com.isa.beans.User;
import com.isa.dto.UserDto;
import com.isa.repository.IUserRepository;
import com.isa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User register(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAddress(userDto.getAddress());
        user.setOccupation(userDto.getOccupation());
        user.setCompanyInfo(userDto.getCompanyInfo());

        userRepository.save(user);
        return user;
    }
}
