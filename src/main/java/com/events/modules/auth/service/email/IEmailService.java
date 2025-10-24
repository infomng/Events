package com.events.modules.auth.service.email;

public interface IEmailService {
    void sendVerificationEmail(String email, String verificationToken);

    void sendResetPasswordEmail(String email, String token);
}
