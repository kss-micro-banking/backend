package com.kss.backend.user.agent;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AgentRepository
 */
public interface AgentRepository extends JpaRepository<Agent, UUID> {

}
