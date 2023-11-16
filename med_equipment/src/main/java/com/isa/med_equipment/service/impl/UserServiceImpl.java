package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.exception.IncorrectPasswordException;
import com.isa.med_equipment.model.Address;
import com.isa.med_equipment.model.RegisteredUser;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.security.token.ConfirmationToken;
import com.isa.med_equipment.security.token.ConfirmationTokenRepository;
import com.isa.med_equipment.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

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
    public Optional<User> emailExists(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User register(UserRegistrationDto userRegistrationDto) throws EmailExistsException {
        RegisteredUser user = new RegisteredUser();

        if(emailExists(userRegistrationDto.getEmail()).isPresent())
            throw new EmailExistsException("Account with email address: " + userRegistrationDto.getEmail() + " already exists");

        user.setName(userRegistrationDto.getName());
        user.setSurname(userRegistrationDto.getSurname());
        user.setOccupation(userRegistrationDto.getOccupation());
        user.setCompanyInfo(userRegistrationDto.getCompanyInfo());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setEmail(userRegistrationDto.getEmail());
        user.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        user.setEnabled(false);

        Address address = new Address();
        address.setStreet(userRegistrationDto.getAddress().getStreet());
        address.setStreetNumber(userRegistrationDto.getAddress().getStreetNumber());
        address.setCity(userRegistrationDto.getAddress().getCity());
        address.setCountry(userRegistrationDto.getAddress().getCountry());
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

    @Override
    @Transactional
    public Optional<User> update(Long userId, UserUpdateDto userUpdateDto) throws IncorrectPasswordException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) return Optional.empty();

        User existingUser = optionalUser.get();

        existingUser.setName(userUpdateDto.getName());
        existingUser.setSurname(userUpdateDto.getSurname());
        if (passwordEncoder.matches(userUpdateDto.getOldPassword(), existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
        } else {
            throw new IncorrectPasswordException("Old password is incorrect");
        }
        existingUser.setPhoneNumber(userUpdateDto.getPhoneNumber());

        if (existingUser instanceof RegisteredUser registeredUser) {
            registeredUser.setOccupation(userUpdateDto.getOccupation());
            registeredUser.setCompanyInfo(userUpdateDto.getCompanyInfo());
            registeredUser.getAddress().updateAddress(
                    userUpdateDto.getAddress().getStreet(),
                    userUpdateDto.getAddress().getStreetNumber(),
                    userUpdateDto.getAddress().getCity(),
                    userUpdateDto.getAddress().getCountry()
            );
        }

        userRepository.save(existingUser);

        return Optional.of(existingUser);
    }
}