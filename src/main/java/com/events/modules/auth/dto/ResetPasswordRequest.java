package com.events.modules.auth.dto;

public record ResetPasswordRequest(String token,
                                  String newPassword) {

}

