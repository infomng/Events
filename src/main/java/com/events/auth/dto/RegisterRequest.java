package com.events.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(String fullName,
                              @Email
                              @NotBlank(message = "Veuillez saisir une adresse email valide")
                              String email,

                              @Pattern(
                                      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&()\\[\\]{}^~#_+=|:;<>,./\\\\-]).{12,}$",
                                      message = "Le mot de passe doit contenir au moins 12 caractères, une majuscule, une minuscule, un chiffre et un symbole."
                              )
                              String password) {
}
