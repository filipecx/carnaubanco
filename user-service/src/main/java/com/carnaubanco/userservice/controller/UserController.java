package com.carnaubanco.userservice.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carnaubanco.userservice.dto.CreateUserRequest;
import com.carnaubanco.userservice.dto.UserResponse;
import com.carnaubanco.userservice.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin (origins = "http://localhost:3000")
@RestController 
@RequestMapping ("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping 
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.create(request);

        URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping 
    public ResponseEntity<List<UserResponse>>  getAllUsers() {
        List<UserResponse> responseList = userService.findAllActiveUsers();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse response = userService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping ("/by-cpf/{cpf}")
    public ResponseEntity<UserResponse> findByCpf(@PathVariable String cpf) {
        UserResponse response = userService.findByCpf(cpf);

        return ResponseEntity.ok(response);
    }

    @GetMapping ("/{id}/active")
    public ResponseEntity<Boolean> isUserActive(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.isUserActive(id));
    }
}
