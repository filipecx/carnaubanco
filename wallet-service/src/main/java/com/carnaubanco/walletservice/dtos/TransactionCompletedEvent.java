package com.carnaubanco.walletservice.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionCompletedEvent(
    UUID transactionId,
    UUID sourceWalletId,
    UUID targetWalletId,
    BigDecimal amount,
    String currency,
    String ocurredAt
) {

}
