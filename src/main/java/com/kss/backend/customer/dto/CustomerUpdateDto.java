package com.kss.backend.customer.dto;

import java.util.UUID;

import com.kss.backend.customer.Customer.CustomerNextOfKin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * CustomerUpdateDto
 */
public record CustomerUpdateDto(
        String name,
        @Pattern(regexp = "^\\d+$", message = "Phone number must contain only numbers") String phoneNumber,
        @Email String email,
        String location,
        CustomerNextOfKin nextOfKin,
        UUID branchId) {
}
