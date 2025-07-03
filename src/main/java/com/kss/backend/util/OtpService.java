package com.kss.backend.util;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * OtpService
 */
@Service
public record OtpService(JavaMailSender mailSender) {
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private static final SecureRandom random = new SecureRandom();

    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("KSS Admin");
        message.setText(String.format("Your OTP is: %s\n\nThis OTP will expire in 5 minutes", otp));

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }

    }
}
