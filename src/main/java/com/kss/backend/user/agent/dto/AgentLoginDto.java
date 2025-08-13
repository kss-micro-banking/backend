package com.kss.backend.user.agent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * AgentLoginDto
 */
public record AgentLoginDto(@NotNull @Email String email) {
}
