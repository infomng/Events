package com.events.auth.service.email;

public interface IEmailService {
    void sendVerificationEmail(String email, String verificationToken);
}
