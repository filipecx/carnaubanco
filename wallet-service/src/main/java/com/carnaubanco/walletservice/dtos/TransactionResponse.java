package com.carnaubanco.walletservice.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


import com.carnaubanco.walletservice.domain.Transaction;
import com.carnaubanco.walletservice.domain.TransactionStatus;
import com.carnaubanco.walletservice.domain.TransactionType;

public record TransactionResponse(
    UUID id,
    String idempotencyKey,
    UUID sourceWalletId,
    UUID targetWalletId,
    BigDecimal amount,
    TransactionType type,
    TransactionStatus status,
    OffsetDateTime createdAt
) {
    public static TransactionResponse fromEntity(Transaction tx) {
        return new TransactionResponse(
            tx.getId(), 
            tx.getIdempotencyKey(), 
            tx.getSourceWallet().getId(), 
            tx.getTargetWallet().getId(), 
            tx.getAmount(), 
            tx.getType(), 
            tx.getStatus(), 
            tx.getCreatedAt()
        );
    }
}
