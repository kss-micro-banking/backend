package com.kss.backend.customer;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kss.backend.branch.Branch;
import com.kss.backend.branch.BranchService;
import com.kss.backend.customer.dto.CustomerCreateDto;
import com.kss.backend.customer.dto.CustomerUpdateDto;

/**
 * CustomerService
 */

@Service
public record CustomerService(
        CustomerRepository customerRepository,
        CustomerMapper customerMapper,
        BranchService branchService) {

    List<Customer> findAll() {
        return customerRepository.findAll();
    }

    Customer findById(UUID id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    Customer create(CustomerCreateDto dto) {
        Branch branch = branchService.findById(dto.branchId());

        Customer customer = new Customer(
                dto.name(),
                dto.phoneNumber(),
                dto.email(),
                dto.location(),
                dto.nextOfKin(),
                branch);

        return customerRepository.save(customer);
    }

    Customer update(UUID id, CustomerUpdateDto dto) {
        Customer existingCustomer = this.findById(id);
        customerMapper.updateFromDto(dto, existingCustomer);
        return customerRepository.save(existingCustomer);
    }

    void delete(UUID id) {
        Customer existingCustomer = this.findById(id);
        customerRepository.delete(existingCustomer);
    }
}
