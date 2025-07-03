package com.kss.backend.user.admin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * adminRepository
 */
public interface AdminRepository extends JpaRepository<Admin, UUID> {

}
