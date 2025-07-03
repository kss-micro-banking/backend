package com.kss.backend.branch.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * CreateBranchDto
 */
public record BranchCreateDto(@NotNull String name, @NotNull String location, @NotNull UUID agentId) {
}
