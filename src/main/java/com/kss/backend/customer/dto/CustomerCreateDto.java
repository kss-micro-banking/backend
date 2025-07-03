package com.kss.backend.customer.dto;

import java.util.UUID;

import com.kss.backend.customer.Customer.CustomerNextOfKin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * CustomerCreateDto
 */
public record CustomerCreateDto(
        @NotNull String name,
        @NotNull @Pattern(regexp = "^\\d+$", message = "Phone number must contain only numbers") String phoneNumber,
        @NotNull @Email String email,
        @NotNull String location,
        @NotNull CustomerNextOfKin nextOfKin,
        @NotNull UUID branchId) {
}
