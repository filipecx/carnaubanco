package com.carnaubanco.userservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.carnaubanco.userservice.domain.User;
import com.carnaubanco.userservice.domain.UserStatus;

public record UserResponse(

    UUID id,
    String name,
    String email,
    String cpf,
    UserStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(), 
            user.getName(), 
            user.getEmail(), 
            user.getCpf(), 
            user.getStatus(),
            user.getCreatedAt(), 
            user.getUpdatedAt()
        );
    }
}
