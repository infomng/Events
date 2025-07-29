package com.events.auth.dto;

public record ResetPasswordRequest(String token,
                                  String newPassword) {

}

