package com.carnaubanco.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank (message = "O nome é obrigatório")
    @Size (min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    String name, 

    @NotBlank (message = "O email é obrigatório")
    @Email (message = "Insira um formato de e-mail válido")
    String email, 

    @NotBlank (message = "CPF obrigatório")
    @Pattern (regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
    String cpf,

    @NotBlank (message = "A senha é obrigatória")
    @Size (min = 3, message = "A senha deve conter no mínimo 3 caracteres")
    String password
) {
}