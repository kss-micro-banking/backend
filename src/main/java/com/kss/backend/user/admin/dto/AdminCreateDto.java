package com.kss.backend.user.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * AdminCreateDto
 */
public record AdminCreateDto(@NotNull String name, @NotNull @Email String email, @NotNull String password,
        boolean superAdmin) {
}
