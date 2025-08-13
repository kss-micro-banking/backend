package com.kss.backend.user.agent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * AddAgentDto
 */
public record AgentCreateDto(@NotNull String name, @NotNull @Email String email) {
}
