package com.carnaubanco.walletservice.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferRequest(

    @NotNull (message = "A carteira de origem é obrigatória")
    UUID sourceWalletId,

    @NotNull (message = "A carteira de destino é obrigatória")
    UUID targetWalletId,

    @NotNull (message = "O valor da transferência é obrigatório")
    @DecimalMin (value = "0.01", message = "O valor mínimo para transferência é 0.01")
    BigDecimal amount
) {

}
