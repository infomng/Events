package com.events.modules.auth.service.email;

import com.events.common.enums.UrlsEnum;
import com.events.common.utils.contants.NameOf;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;


    @Async
    @Override
    public void sendVerificationEmail(String email, String verificationToken) {
        String path = UrlsEnum.VERIFY_EMAIL.getPath();
        sendEmail(
                email,
                verificationToken,
                NameOf.EMAIL_VERIFICATION,
                path,
                NameOf.CLICK_THE_BUTTON_BELOW_TO_VERIFY_YOUR_EMAIL_ADDRESS);
    }

    @Async
    @Override
    public void sendResetPasswordEmail(String email, String token) {
        String path = UrlsEnum.RESET_PASSWORD.getPath();
        sendEmail(
                email,
                token,
                NameOf.PASSWORD_RESET_REQUEST,
                path,
                NameOf.CLICK_THE_BUTTON_BELOW_TO_RESET_YOUR_PASSWORD);
    }

    private void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            String actionUrl = baseUrl + path + "?token=" + token;

            String content = NameOf.SEND_VERIFICATION_EMAIL_CONTENT.formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            LOGGER.error(NameOf.FAILED_TO_SEND_EMAIL, e.getMessage(), e);
        }
    }
}
