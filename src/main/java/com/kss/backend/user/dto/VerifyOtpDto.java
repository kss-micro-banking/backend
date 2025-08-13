package com.kss.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * VerifyOtpDto
 */
public record VerifyOtpDto(@NotNull @Email String email, @NotNull String otp) {
}
