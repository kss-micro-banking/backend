package com.kss.backend.user.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * SendPasswordResetLinkDto
 */
public record SendPasswordResetLinkDto(@Email @NotNull String email) {
}
