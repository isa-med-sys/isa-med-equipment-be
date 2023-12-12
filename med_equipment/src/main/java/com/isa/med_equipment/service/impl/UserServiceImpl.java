package com.isa.med_equipment.service.impl;

import com.isa.med_equipment.dto.CompanyAdminRegistrationDto;
import com.isa.med_equipment.dto.SystemAdminRegistrationDto;
import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.dto.*;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.exception.IncorrectPasswordException;
import com.isa.med_equipment.model.*;
import com.isa.med_equipment.repository.CompanyRepository;
import com.isa.med_equipment.repository.UserRepository;
import com.isa.med_equipment.security.token.ConfirmationToken;
import com.isa.med_equipment.security.token.ConfirmationTokenRepository;
import com.isa.med_equipment.service.UserService;

import com.isa.med_equipment.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CompanyRepository companyRepository,
                           ConfirmationTokenRepository confirmationTokenRepository,
                           EmailSenderService emailSenderService,
                           PasswordEncoder passwordEncoder,
                           Mapper mapper) {
        super();
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<CompanyAdmin> findByCompanyId(Long id) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user instanceof CompanyAdmin)
                .map(user -> (CompanyAdmin) user)
                .filter(admin -> admin.getCompany() != null && admin.getCompany().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID %d not found!", id)));
        return mapper.map(user, UserDto.class);
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
    public UserDto update(Long id, UserUpdateDto userUpdateDto) throws IncorrectPasswordException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID %d not found!", id)));

        validateCurrentPassword(userUpdateDto.getCurrentPassword(), user);
        updateUserData(user, userUpdateDto);
        userRepository.save(user);

        return mapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Boolean changePassword(Long userId, String password) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) { return false; }

        User existingUser = optionalUser.get();
        if(password != null && !password.isBlank() && !getPasswordChange(userId)) {
            if (existingUser instanceof SystemAdmin systemAdmin) {
                ((SystemAdmin) existingUser).setHasChangedPassword(true);
                existingUser.setPassword(passwordEncoder.encode(password));
                userRepository.save(existingUser);
                return true;
            }
        }

        return false;
    }

    @Override
    public CompanyAdmin registerCompanyAdmin(CompanyAdminRegistrationDto companyAdminRegistrationDto) throws EmailExistsException {
        CompanyAdmin companyAdmin = new CompanyAdmin();

        if(emailExists(companyAdminRegistrationDto.getEmail()).isPresent())
            throw new EmailExistsException("Account with email address: " + companyAdminRegistrationDto.getEmail() + " already exists");

        companyAdmin.setName(companyAdminRegistrationDto.getName());
        companyAdmin.setSurname(companyAdminRegistrationDto.getSurname());

        Optional<Company> retrievedCompanyOptional = companyRepository.findById(companyAdminRegistrationDto.getCompanyId());
        Company company = retrievedCompanyOptional.orElse(new Company());

        companyAdmin.setCompany(company);
        companyAdmin.setPassword(passwordEncoder.encode(companyAdminRegistrationDto.getPassword()));
        companyAdmin.setEmail(companyAdminRegistrationDto.getEmail());
        companyAdmin.setPhoneNumber(companyAdminRegistrationDto.getPhoneNumber());
        companyAdmin.setEnabled(true);

        userRepository.save(companyAdmin);

        return companyAdmin;
    }

    @Override
    public SystemAdmin registerSystemAdmin(SystemAdminRegistrationDto systemAdminRegistrationDto) throws EmailExistsException {
        SystemAdmin systemAdmin = new SystemAdmin();

        if(emailExists(systemAdminRegistrationDto.getEmail()).isPresent())
            throw new EmailExistsException("Account with email address: " + systemAdminRegistrationDto.getEmail() + " already exists");

        systemAdmin.setName(systemAdminRegistrationDto.getName());
        systemAdmin.setSurname(systemAdminRegistrationDto.getSurname());

        systemAdmin.setPassword(passwordEncoder.encode(systemAdminRegistrationDto.getPassword()));
        systemAdmin.setEmail(systemAdminRegistrationDto.getEmail());
        systemAdmin.setPhoneNumber(systemAdminRegistrationDto.getPhoneNumber());
        systemAdmin.setEnabled(true);
        systemAdmin.setHasChangedPassword(false);

        userRepository.save(systemAdmin);

        return systemAdmin;
    }

    @Override
    public Boolean getPasswordChange(Long id) { //brziGolub
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user instanceof SystemAdmin systemAdmin) {
                return systemAdmin.getHasChangedPassword();
            } else {
                return true;
            }
        }
        return true;
    }

    private void validateCurrentPassword(String currentPassword, User existingUser) throws IncorrectPasswordException {
        if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
            throw new IncorrectPasswordException("Current password is incorrect");
        }
    }

    private void updateUserData(User existingUser, UserUpdateDto userUpdateDto) {
        existingUser.setName(userUpdateDto.getName());
        existingUser.setSurname(userUpdateDto.getSurname());
        existingUser.setPassword(encodePassword(userUpdateDto.getNewPassword(), userUpdateDto.getCurrentPassword()));
        existingUser.setPhoneNumber(userUpdateDto.getPhoneNumber());

        if (existingUser instanceof RegisteredUser registeredUser) {
            updateRegisteredUserData(registeredUser, userUpdateDto);
        }
    }

    private String encodePassword(String newPassword, String currentPassword) {
        boolean hasNewPassword = newPassword != null && !newPassword.isBlank();
        return hasNewPassword ? passwordEncoder.encode(newPassword) : passwordEncoder.encode(currentPassword);
    }

    private void updateRegisteredUserData(RegisteredUser registeredUser, UserUpdateDto userUpdateDto) {
        registeredUser.setOccupation(userUpdateDto.getOccupation());
        registeredUser.setCompanyInfo(userUpdateDto.getCompanyInfo());
        registeredUser.getAddress().updateAddress(
                userUpdateDto.getAddress().getStreet(),
                userUpdateDto.getAddress().getStreetNumber(),
                userUpdateDto.getAddress().getCity(),
                userUpdateDto.getAddress().getCountry()
        );
    }
}