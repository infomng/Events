package com.events.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank(message = "Adresse email invalide")
        String email,

        @NotBlank(message = "Mot de passe invalide")
        String password) {
}
