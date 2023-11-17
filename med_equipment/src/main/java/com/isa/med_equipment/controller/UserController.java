package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.CompanyAdminRegistrationDto;
import com.isa.med_equipment.dto.UserRegistrationDto;
import com.isa.med_equipment.dto.UserUpdateDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.exception.IncorrectPasswordException;
import com.isa.med_equipment.model.CompanyAdmin;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.security.authentication.AuthenticationRequest;
import com.isa.med_equipment.security.authentication.AuthenticationResponse;
import com.isa.med_equipment.service.AuthenticationService;
import com.isa.med_equipment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
            User registeredUser = userService.register(userRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }

    @PostMapping("/register-company-admin")
    //@PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and #id == authentication.principal.id") //
    public ResponseEntity<?> registerCompanyAdmin(@RequestBody CompanyAdminRegistrationDto companyAdminRegistrationDto) {
        try {
            CompanyAdmin registeredCompanyAdmin = userService.registerCompanyAdmin(companyAdminRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompanyAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        try {
            User confirmedUser = userService.confirmRegistration(token);
            return (confirmedUser != null) ? ResponseEntity.ok("Account successfully confirmed.") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the confirmation.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER') and #id == authentication.principal.id")
    public ResponseEntity<Optional<User>> getById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.isPresent() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER') and #id == authentication.principal.id")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        try {
            Optional<User> updatedUser = userService.update(id, userUpdateDto);
            return updatedUser.map(user -> ResponseEntity.ok().body(user))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }
    }
}