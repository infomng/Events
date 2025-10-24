package com.events.common.utils.contants;

public final class NameOf {
    public static final String EMAIL = "email";
    public static final String FULL_NAME = "fullName";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String FAILED_TO_SEND_EMAIL = "Failed to send email: {}";
    public static final String EMAIL_VERIFICATION = "Email Verification";
    public static final String PLEASE_CHECK_YOUR_INBOX = ". Please check your inbox.";
    public static final String VERIFICATION_EMAIL_SENT_TO = "Verification email sent to ";
    public static final String CLICK_THE_BUTTON_BELOW_TO_VERIFY_YOUR_EMAIL_ADDRESS = "Click the button below to verify your email address:";
    public static final String PASSWORD_RESET_REQUEST = "Password Reset Request";
    public static final String CLICK_THE_BUTTON_BELOW_TO_RESET_YOUR_PASSWORD = "Click the button below to reset your password:";
    public static final String SEND_VERIFICATION_EMAIL_CONTENT = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                    <h2 style="color: #333;">%s</h2>
                    <p style="font-size: 16px; color: #555;">%s</p>
                    <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Proceed</a>
                    <p style="font-size: 14px; color: #777;">Or copy and paste this link into your browser:</p>
                    <p style="font-size: 14px; color: #007bff;">%s</p>
                    <p style="font-size: 12px; color: #aaa;">This is an automated message. Please do not reply.</p>
                </div>
            """;
}
