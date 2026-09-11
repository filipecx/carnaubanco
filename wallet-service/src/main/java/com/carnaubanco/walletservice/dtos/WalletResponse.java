package com.carnaubanco.walletservice.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.carnaubanco.walletservice.domain.Wallet;
import com.carnaubanco.walletservice.domain.WalletStatus;

public record WalletResponse(
    UUID id,
    UUID userId,
    String currency,
    BigDecimal balance,
    WalletStatus status,
    OffsetDateTime createdAt
) {
    public static WalletResponse fromEntity(Wallet wallet) {
        return new WalletResponse(
            wallet.getId(),
            wallet.getUserId(), 
            wallet.getCurrency(), 
            wallet.getBalance(), 
            wallet.getStatus(),
            wallet.getCreatedAt()
    );
    }
}
