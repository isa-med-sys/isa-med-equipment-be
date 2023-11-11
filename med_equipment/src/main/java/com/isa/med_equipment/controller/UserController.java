package com.isa.med_equipment.controller;

import java.util.List;
import java.util.Optional;

import com.isa.med_equipment.beans.ConfirmationToken;
import com.isa.med_equipment.beans.User;
import com.isa.med_equipment.service.impl.UserService;
import com.isa.med_equipment.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Optional<User>> getById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.isPresent() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            boolean emailExists = userService.emailExists(userDto.getEmail());
            if (emailExists)
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("User with email " + userDto.getEmail() + " already exists.");
            User registeredUser = userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
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
}