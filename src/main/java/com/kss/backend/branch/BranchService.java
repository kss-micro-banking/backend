package com.kss.backend.branch;

import java.util.List;
import java.util.UUID;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kss.backend.branch.dto.BranchCreateDto;
import com.kss.backend.user.User;
import com.kss.backend.user.agent.Agent;
import com.kss.backend.user.agent.AgentService;

/**
 * BranchService
 */
@Service
public record BranchService(BranchRepository branchRepository, AgentService agentService) {

    // private static Logger log = LoggerFactory.getLogger(BranchService.class);

    List<Branch> findAll() {
        return branchRepository.findAll();
    }

    Branch findByAgent(User agent) {
        return branchRepository.findByAgent(agent)
                .orElseThrow(() -> new RuntimeException("Branch with agent does not exist"));
    }

    public Branch findById(UUID id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch does not exist"));
    }

    Branch create(BranchCreateDto dto) {
        Agent agent = agentService.getAgentById(dto.agentId());
        Branch branch = new Branch(dto.name(), dto.location());
        branch.setAgent(agent);
        return branchRepository.save(branch);
    }

    void delete(UUID id) {
        this.findById(id);
        branchRepository.deleteById(id);
    }

}
