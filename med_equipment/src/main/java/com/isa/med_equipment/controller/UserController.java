package com.isa.med_equipment.controller;

import com.isa.med_equipment.dto.UserDto;
import com.isa.med_equipment.exception.EmailExistsException;
import com.isa.med_equipment.model.User;
import com.isa.med_equipment.security.authentication.AuthenticationRequest;
import com.isa.med_equipment.security.authentication.AuthenticationResponse;
import com.isa.med_equipment.service.AuthenticationService;
import com.isa.med_equipment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            User registeredUser = userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
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

    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.isPresent() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}