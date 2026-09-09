package com.carnaubanco.userservice.domain;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "users")
public class User {
    @Id 
    private UUID id;

    @Column (nullable = false, length = 150)
    private String name;

    @Column (nullable = false, unique = true, length = 150)
    private String email;

    @Column (nullable = false, unique = true, length = 14)
    private String cpf;

    @Column (name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    private UserStatus status;

    @Column (name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column (name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected User() {}

    public User(String name, String email, String cpf, String passwordHash) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.passwordHash = passwordHash;
        this.status = UserStatus.ACTIVE;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void block() {
        this.status = UserStatus.BLOCKED;
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
//saber se dois objetos tem os mesmos dados. Por padrão a equals só daria true se fosse o mesmo endereço de memória
    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id); //compare o id dos dois objetos
    }

    @Override 
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
