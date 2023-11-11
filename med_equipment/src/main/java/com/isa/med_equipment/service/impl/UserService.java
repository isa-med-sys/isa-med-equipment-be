package com.isa.med_equipment.service.impl;

import java.util.List;
import java.util.Optional;
import com.isa.med_equipment.beans.User;
import com.isa.med_equipment.beans.Address;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.repository.ConfirmationTokenRepository;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.isa.med_equipment.beans.ConfirmationToken;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    final EmailSenderService emailSenderService;

    @Autowired
    public UserService(UserRepository userRepository, ConfirmationTokenRepository confirmationTokenRepository, EmailSenderService emailSenderService) {
        super();
        this.userRepository = userRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
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

    // TODO protect password

    @Override
    public User register(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setOccupation(userDto.getOccupation());
        user.setCompanyInfo(userDto.getCompanyInfo());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEnabled(false);

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
