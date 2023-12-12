package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CompanyAdminRegistrationDto;
import com.isa.med_equipment.dto.SystemAdminRegistrationDto;
import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.SystemAdmin;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.security.authentication.AuthenticationRequest;
import com.isa.med_equipment.security.authentication.AuthenticationResponse;
import com.isa.med_equipment.service.AuthenticationService;
import com.isa.med_equipment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            UserRegistrationDto registeredUser = userService.register(userRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register-company-admin")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<?> registerCompanyAdmin(@RequestBody CompanyAdminRegistrationDto companyAdminRegistrationDto) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal);
        try {
            CompanyAdmin registeredCompanyAdmin = userService.registerCompanyAdmin(companyAdminRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompanyAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }

    @PostMapping("/register-system-admin")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<?> registerSystemAdmin(@RequestBody SystemAdminRegistrationDto systemAdminRegistrationDto) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal: " + principal);
        try {
            SystemAdmin registeredSystemAdmin = userService.registerSystemAdmin(systemAdminRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredSystemAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }

    @GetMapping("/company/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<List<CompanyAdmin>> getByCompanyId(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findByCompanyId(id), HttpStatus.OK);
    }

    @GetMapping("/password-change/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<Boolean> getPasswordChange(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getPasswordChange(id), HttpStatus.OK);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        try {
            UserRegistrationDto confirmedUser = userService.confirmRegistration(token);
            return (confirmedUser != null) ? ResponseEntity.ok("Account successfully confirmed.") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("(hasAnyRole('ROLE_REGISTERED_USER', 'ROLE_COMPANY_ADMIN') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto result = userService.findById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasAnyRole('ROLE_REGISTERED_USER', 'ROLE_COMPANY_ADMIN') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        UserDto result = userService.update(id, userUpdateDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/password/{id}")
    @PreAuthorize("(hasAnyRole('ROLE_SYSTEM_ADMIN', 'ROLE_COMPANY_ADMIN') and #id == authentication.principal.id)")
    public ResponseEntity<Boolean> changePassword(@PathVariable Long id, @RequestBody String pass) {
        return new ResponseEntity<>(userService.changePassword(id, pass), HttpStatus.OK);
    }
}