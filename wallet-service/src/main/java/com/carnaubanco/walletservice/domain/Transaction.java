package com.carnaubanco.walletservice.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "transactions")
public class Transaction {

    @Id 
    private UUID id;

    @Column (name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "source_wallet_id", nullable = false)
    private Wallet sourceWallet;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "target_wallet_id", nullable = false)
    private Wallet targetWallet;

    @Column (nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 30)
    private TransactionType type;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    private TransactionStatus status;

    @Column (name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected Transaction() {}

    public Transaction(
        String idempotencyKey, 
        Wallet sourceWallet,
        Wallet targetWallet, 
        BigDecimal amount, 
        TransactionType type,
        TransactionStatus status
    ) {
            this.id = UUID.randomUUID();
            this.idempotencyKey = idempotencyKey;
            this.sourceWallet = sourceWallet;
            this.targetWallet = targetWallet;
            this.amount = amount;
            this.type = type;
            this.status = status;
            this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Wallet getSourceWallet() {
        return sourceWallet;
    }

    public Wallet getTargetWallet() {
        return targetWallet;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(id);
    }
}
