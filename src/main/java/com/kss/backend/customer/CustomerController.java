package com.kss.backend.customer;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kss.backend.customer.dto.CustomerCreateDto;
import com.kss.backend.customer.dto.CustomerUpdateDto;
import com.kss.backend.util.Api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * CustomerController
 */
@RestController
@RequestMapping(path = Api.Routes.CUSTOMER)
@Tag(name = "Customer Enpoints")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    ResponseEntity<List<Customer>> findAll() {
        var customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    ResponseEntity<Customer> create(@Valid @RequestBody CustomerCreateDto dto) {
        var customer = customerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{customerId}")
    ResponseEntity<Customer> findById(@PathVariable UUID customerId) {
        var customer = customerService.findById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PatchMapping("/{customerId}")
    ResponseEntity<Customer> update(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateDto dto) {
        var customer = customerService.update(customerId, dto);
        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{customerId}")
    ResponseEntity<Void> delete(@PathVariable UUID customerId) {
        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }

}
