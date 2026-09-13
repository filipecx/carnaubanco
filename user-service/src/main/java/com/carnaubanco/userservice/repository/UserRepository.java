package com.carnaubanco.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carnaubanco.userservice.domain.User;
import com.carnaubanco.userservice.domain.UserStatus;

@Repository 
public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    List<User> findByStatus(UserStatus status);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
