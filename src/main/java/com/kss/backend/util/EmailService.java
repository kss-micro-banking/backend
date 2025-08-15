package com.kss.backend.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

/**
 * EmailService
 */
@Service
public record EmailService(
    JavaMailSender mailSender,
    PasswordEncoder passwordEncoder,
    @Value("${app.client-url}") String clientUrl) {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);
  private static final SecureRandom random = new SecureRandom();

  public String generateOtp() {
    return String.format("%06d", random.nextInt(1000000));
  }

  public void sendMail(OtpUser user, String subject, String content) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

      message.setTo(user.getEmail());
      message.setSubject(subject);
      message.setText(content, true);

      mailSender.send(mimeMessage);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("Failed to send email", e);
    }
  }

  public void sendOtp(OtpUser user) {
    String otp = generateOtp();

    sendMail(
        user,
        "KSS ADMIN | Login OTP",
        String.format("""
            Your OTP is: %s\n\nThis OTP will expire in 5 minutes
            """, otp));

    user.setOtp(passwordEncoder.encode(otp));
    user.setOtpExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));
  }

  public void verifyOtp(OtpUser user, String otp, boolean encoded) {
    if (user.getOtp() == null || user.getOtpExpiry() == null) {
      throw new RuntimeException("No otp requested");
    }

    if (Instant.now().isAfter(user.getOtpExpiry()))
      throw new RuntimeException("Invalid OTP: Expired");

    boolean isMatch = encoded ? otp.equals(user.getOtp()) : passwordEncoder.matches(otp, user.getOtp());

    if (!isMatch)
      throw new RuntimeException("Invalid OTP: Mismatch");

    user.setOtp(null);
    user.setOtpExpiry(null);
  }

  public void sendPasswordResetLink(OtpUser user) {
    String otp = passwordEncoder.encode(generateOtp());
    user.setOtp(otp);
    user.setOtpExpiry(Instant.now().plus(5, ChronoUnit.MINUTES));

    String link = String.format("%s/reset-password?token=%s", clientUrl, otp);

    sendMail(user,
        "KSS Admin | Reset Password",
        String.format("""
            <html>
              <body>
                <p>You have requested to reset your password.</p>
                <p>Click the link below to proceed:</p>
                <p><a href="%s" target="_blank">Reset Password</a></p>
                <p>Please <strong>IGNORE</strong> this mail if you did not initiate this
                request.</p>
              </body>
            </html>
            """, link));
  }

}
