package com.carnaubanco.userservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carnaubanco.userservice.domain.User;
import com.carnaubanco.userservice.domain.UserStatus;
import com.carnaubanco.userservice.dto.CreateUserRequest;
import com.carnaubanco.userservice.dto.UserResponse;
import com.carnaubanco.userservice.exception.UserAlreadyExistsException;
import com.carnaubanco.userservice.exception.UserNotFoundException;
import com.carnaubanco.userservice.repository.UserRepository;

@Service 
@Transactional 
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Transactional 
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Já existe um usuário cadastrado com esse e-mail");
        }
        if (repository.existsByCpf(request.cpf())) {
            throw new UserAlreadyExistsException("Já existe um usuário cadastrado com esse CPF");
        }
        String passwordHash = encoder.encode(request.password());

        User user = new User(request.name(), request.email(), request.cpf(), passwordHash);

        User savedUser = repository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    public UserResponse findById(UUID id) {
        return repository.findById(id)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new UserNotFoundException("Usuário com ID: " + id + " não encontrado"));
    }

    public UserResponse findByEmail(String email) {
        return repository.findByEmail(email)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new UserNotFoundException("Nenhum usuário com e-mail " + email + " encontrado"));
    }

    public UserResponse findByCpf(String cpf) {
        return repository.findByEmail(cpf)
        .map(UserResponse::fromEntity)
        .orElseThrow(() -> new UserNotFoundException("Nenhum usuário com CPF " + cpf + " encontrado"));
    }

    public List<UserResponse> findAllActiveUsers() {
        return repository.findByStatus(UserStatus.ACTIVE)
        .stream()
        .map(UserResponse::fromEntity)
        .toList();
    }

    public Boolean isUserActive(UUID id) {
        User user = repository.findById(id).orElseThrow(
            () -> new UserNotFoundException("Nenhum usuário com o id " + id + " encontrado")
        );

        if (user.getStatus() == UserStatus.ACTIVE) {
            return true;
        }
        return false;
    }
}
