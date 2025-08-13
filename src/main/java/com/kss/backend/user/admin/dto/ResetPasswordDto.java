package com.kss.backend.user.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * ResetPasswordDto
 */
public record ResetPasswordDto(@Email @NotNull String email, @NotNull String newPassword) {
}
