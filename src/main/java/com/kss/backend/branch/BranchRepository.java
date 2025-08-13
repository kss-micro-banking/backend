package com.kss.backend.branch;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kss.backend.user.User;

/**
 * BranchRepository
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    Optional<Branch> findByAgent(User agent);
}
