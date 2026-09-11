package com.carnaubanco.walletservice.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateWalletRequest(
    @NotNull (message = "O ID do usuário é obrigatório")
    UUID userId,
    String currency
) {

}
