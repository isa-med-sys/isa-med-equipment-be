package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.ConfirmationToken;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.security.token.ConfirmationTokenRepository;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.UserService;
import com.isa.med_equipment.exception.EmailExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
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
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User register(UserDto userDto) throws EmailExistsException {
        User user = new User();

        if(emailExists(userDto.getEmail()))
            throw new EmailExistsException("Account with email address: " + userDto.getEmail() + " already exists");

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setOccupation(userDto.getOccupation());
        user.setCompanyInfo(userDto.getCompanyInfo());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEnabled(false);
        user.setRole(userDto.getRole());

        Address address = new Address();
        address.setStreet(userDto.getAddress().getStreet());
        address.setStreetNumber(userDto.getAddress().getStreetNumber());
        address.setCity(userDto.getAddress().getCity());
        address.setCountry(userDto.getAddress().getCountry());
        user.setAddress(address);

        userRepository.save(user);

        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);

        String confirmationLink = "http://localhost:8080/api/users/confirm-account?token=" + token.getConfirmationToken();
        emailSenderService.sendEmail(user, confirmationLink);

        return user;
    }

    public User confirmRegistration(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = token.getUser();

        if(user != null) {
            user.setEnabled(true);
            userRepository.save(user);
        }

        return user;
    }
}