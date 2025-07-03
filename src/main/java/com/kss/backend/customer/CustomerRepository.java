package com.kss.backend.customer;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CustomerRepository
 */
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
