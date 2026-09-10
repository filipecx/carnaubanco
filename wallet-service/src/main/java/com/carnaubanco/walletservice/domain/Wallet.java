package com.carnaubanco.walletservice.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "wallets")
public class Wallet {

    @Id 
    private UUID id;

    @Column (name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column (nullable = false, length = 10)
    private String currency;

    @Column (nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    private WalletStatus status;

    @Column (name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column (name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Wallet() {}

    public Wallet(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.currency = "PALHA";
        this.balance = BigDecimal.ZERO.setScale(2);
        this.status = WalletStatus.ACTIVE;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência não pode ser igual ou menor que zero");
        }
        if (!isActive()) {
            throw new IllegalArgumentException("Não é possível creditar em uma carteira inativa");
        }
        this.balance = this.balance.add(amount);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência não pode ser igual ou menor que zero");
        }
        if (!isActive()) {
            throw new IllegalArgumentException("Não é possível debitar de uma carteira inativa");
        }

        if (this.balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public boolean isActive() {
        return this.status == WalletStatus.ACTIVE;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(id);
    }
}
