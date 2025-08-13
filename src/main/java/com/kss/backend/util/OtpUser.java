package com.kss.backend.util;

import java.time.Instant;

/**
 * OtpUser
 */
public interface OtpUser {

  String getEmail();

  void setOtp(String otp);

  String getOtp();

  void setOtpExpiry(Instant otpExpiry);

  Instant getOtpExpiry();
}
