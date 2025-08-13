package com.kss.backend.user.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * AdminLoginDto
 */
public record AdminLoginDto(@NotNull @Email String email, @NotNull String password) {
}
